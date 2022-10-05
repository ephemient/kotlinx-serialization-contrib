package com.github.ephemient.kotlinx.serialization.contrib.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.float
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

/** [Serde](https://serde.rs/)-like JSON format */
@ExperimentalSerializationApi
class Serde(val json: Json = Json) : StringFormat {
    override val serializersModule: SerializersModule
        get() = json.serializersModule
    fun <T> encodeToJsonElement(serializer: SerializationStrategy<T>, value: T): JsonElement =
        SerdeEncoder(json).apply { encodeSerializableValue(serializer, value) }.jsonElement
    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        json.encodeToString(JsonElement.serializer(), encodeToJsonElement(serializer, value))
    fun <T> decodeFromJsonElement(deserializer: DeserializationStrategy<T>, jsonElement: JsonElement): T =
        SerdeDecoder(json, jsonElement).decodeSerializableValue(deserializer)
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        decodeFromJsonElement(deserializer, json.decodeFromString(JsonElement.serializer(), string))
}

@ExperimentalSerializationApi
inline fun <reified T> Serde.encodeToJsonElement(value: T): JsonElement =
    encodeToJsonElement(serializer(), value)

@ExperimentalSerializationApi
inline fun <reified T> Serde.decodeFromJsonElement(jsonElement: JsonElement): T =
    decodeFromJsonElement(serializer(), jsonElement)

@ExperimentalSerializationApi
private abstract class BaseSerdeEncoder(override val json: Json) : AbstractEncoder(), JsonEncoder {
    override val serializersModule: SerializersModule
        get() = json.serializersModule
    protected lateinit var elementName: String
        private set
    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        elementName = descriptor.getElementName(index)
        return true
    }
    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean =
        json.configuration.encodeDefaults
    override fun encodeNull() = encodeJsonElement(JsonNull)
    override fun encodeBoolean(value: Boolean) = encodeJsonElement(JsonPrimitive(value))
    override fun encodeByte(value: Byte): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeShort(value: Short): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeInt(value: Int): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeLong(value: Long): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeFloat(value: Float): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeDouble(value: Double): Unit = encodeJsonElement(JsonPrimitive(value))
    override fun encodeChar(value: Char): Unit = encodeString(value.toString())
    override fun encodeString(value: String) = encodeJsonElement(JsonPrimitive(value))
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = encodeString(enumDescriptor.getElementName(index))
    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = when (descriptor.kind) {
        is StructureKind.LIST -> SerdeListEncoder(json, ::encodeJsonElement)
        is StructureKind.MAP -> SerdeMapEncoder(json, ::encodeJsonElement)
        is PolymorphicKind -> SerdePolymorphicEncoder(json, elementName, ::encodeJsonElement)
        else -> SerdeClassEncoder(json, ::encodeJsonElement)
    }
}

@ExperimentalSerializationApi
private class SerdeEncoder(json: Json) : BaseSerdeEncoder(json) {
    lateinit var jsonElement: JsonElement
        private set
    override fun encodeJsonElement(element: JsonElement) {
        jsonElement = element
    }
}

@ExperimentalSerializationApi
private class SerdeListEncoder(json: Json, private val onEnd: (JsonArray) -> Unit) : BaseSerdeEncoder(json) {
    private val content = mutableListOf<JsonElement>()
    override fun encodeJsonElement(element: JsonElement) {
        content += element
    }
    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor)
        onEnd(JsonArray(content))
    }
}

@ExperimentalSerializationApi
private class SerdeMapEncoder(json: Json, private val onEnd: (JsonObject) -> Unit) : BaseSerdeEncoder(json) {
    private val content = mutableMapOf<String, JsonElement>()
    private var key: String? = null
    override fun encodeString(value: String) {
        if (key == null) key = value else super.encodeString(value)
    }
    override fun encodeJsonElement(element: JsonElement) {
        content[key!!] = element
        key = null
    }
    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor)
        onEnd(JsonObject(content))
    }
}

@ExperimentalSerializationApi
private class SerdePolymorphicEncoder(
    json: Json,
    private val serialName: String,
    private val onEnd: (JsonObject) -> Unit,
) : BaseSerdeEncoder(json) {
    private val content = mutableListOf<JsonElement>()
    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        check(index == content.size)
        return true
    }
    override fun encodeJsonElement(element: JsonElement) {
        content += element
    }
    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean = true
    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor)
        onEnd(buildJsonObject { put(serialName, JsonArray(content)) })
    }
}

@ExperimentalSerializationApi
private class SerdeClassEncoder(json: Json, private val onEnd: (JsonObject) -> Unit) : BaseSerdeEncoder(json) {
    private val content = mutableMapOf<String, JsonElement>()
    override fun encodeJsonElement(element: JsonElement) {
        content[elementName] = element
    }
    override fun encodeNull() {
        if (json.configuration.explicitNulls) super.encodeNull()
    }
    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor)
        onEnd(JsonObject(content))
    }
}

@ExperimentalSerializationApi
private abstract class BaseSerdeDecoder(override val json: Json) : AbstractDecoder(), JsonDecoder {
    override val serializersModule: SerializersModule
        get() = json.serializersModule
    override fun decodeBoolean(): Boolean = with(decodeJsonElement().jsonPrimitive) {
        require(!isString || json.configuration.isLenient)
        boolean
    }
    override fun decodeByte(): Byte = decodeInt().toByte()
    override fun decodeShort(): Short = decodeInt().toShort()
    override fun decodeInt(): Int = with(decodeJsonElement().jsonPrimitive) {
        require(!isString || json.configuration.isLenient)
        int
    }
    override fun decodeLong(): Long = with(decodeJsonElement().jsonPrimitive) {
        require(!isString || json.configuration.isLenient)
        long
    }
    override fun decodeFloat(): Float = with(decodeJsonElement().jsonPrimitive) {
        require(!isString || json.configuration.isLenient)
        float
    }
    override fun decodeDouble(): Double = with(decodeJsonElement().jsonPrimitive) {
        require(!isString || json.configuration.isLenient)
        double
    }
    override fun decodeChar(): Char = decodeString().single()
    override fun decodeString(): String = with(decodeJsonElement().jsonPrimitive) {
        require(isString || json.configuration.isLenient)
        content
    }
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = enumDescriptor.getElementIndex(decodeString())
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = when (descriptor.kind) {
        is StructureKind.LIST -> SerdeListDecoder(json, decodeJsonElement().jsonArray)
        is StructureKind.MAP -> SerdeMapDecoder(json, decodeJsonElement().jsonObject)
        is PolymorphicKind -> SerdePolymorphicDecoder(json, descriptor, decodeJsonElement().jsonObject)
        else -> SerdeClassDecoder(json, decodeJsonElement().jsonObject)
    }
}

@ExperimentalSerializationApi
private class SerdeDecoder(json: Json, private val jsonElement: JsonElement) : BaseSerdeDecoder(json) {
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = throw SerializationException("missing beginStructure")
    override fun decodeNotNullMark(): Boolean = jsonElement != JsonNull
    override fun decodeJsonElement(): JsonElement = jsonElement
}

@ExperimentalSerializationApi
private class SerdeListDecoder(json: Json, private val jsonArray: JsonArray) : BaseSerdeDecoder(json) {
    private var index = 0
    override fun decodeSequentially(): Boolean = true
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = jsonArray.size
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < jsonArray.size) index else CompositeDecoder.DECODE_DONE
    override fun decodeNotNullMark(): Boolean = jsonArray[index] != JsonNull
    override fun decodeNull(): Nothing? = null.also { index++ }
    override fun decodeJsonElement(): JsonElement = jsonArray[index++]
}

@ExperimentalSerializationApi
private class SerdeMapDecoder(json: Json, jsonObject: JsonObject) : BaseSerdeDecoder(json) {
    private var entries = jsonObject.entries.toList()
    private var index = 0
    override fun decodeSequentially(): Boolean = true
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = entries.size
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < 2 * entries.size) index else CompositeDecoder.DECODE_DONE
    override fun decodeNotNullMark(): Boolean {
        check(index % 2 == 1)
        return entries[index / 2].value != JsonNull
    }
    override fun decodeNull(): Nothing? = null.also { index++ }
    override fun decodeJsonElement(): JsonElement {
        check(index % 2 == 1)
        return entries[index++ / 2].value
    }
    override fun decodeString(): String = if (index % 2 == 0) entries[index++ / 2].key else super.decodeString()
}

@ExperimentalSerializationApi
private class SerdePolymorphicDecoder(json: Json, private val descriptor: SerialDescriptor, jsonObject: JsonObject) : BaseSerdeDecoder(json) {
    private val typeIndex = descriptor.getElementIndex("type")
    private val valueIndex = descriptor.getElementIndex("value")
    private val entry = jsonObject.entries.single()
    private var state = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = when (state) {
        0 -> typeIndex
        1 -> valueIndex
        else -> CompositeDecoder.DECODE_DONE
    }
    override fun decodeJsonElement(): JsonElement = when (state) {
        0 -> JsonPrimitive(entry.key)
        1 -> buildJsonObject {
            entry.value.jsonArray.forEachIndexed { i, element -> put(descriptor.getElementName(i), element) }
        }
        else -> throw SerializationException("index out of bounds")
    }.also { state++ }
    override fun decodeString(): String {
        check(state == 0)
        return entry.key.also { state++ }
    }
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        check(state == 1)
        return SerdeListDecoder(json, entry.value.jsonArray).also { state++ }
    }
}

@ExperimentalSerializationApi
private class SerdeClassDecoder(json: Json, jsonObject: JsonObject) : BaseSerdeDecoder(json) {
    private var entries = jsonObject.entries.toList()
    private var index = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < entries.size) descriptor.getElementIndex(entries[index].key) else CompositeDecoder.DECODE_DONE
    override fun decodeNotNullMark(): Boolean = entries[index].value != JsonNull
    override fun decodeNull(): Nothing? = null.also { index++ }
    override fun decodeJsonElement(): JsonElement = entries[index++].value
}
