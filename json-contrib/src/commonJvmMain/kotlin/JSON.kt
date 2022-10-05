@file:OptIn(ExperimentalSerializationApi::class)

package com.github.ephemient.kotlinx.serialization.contrib.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

sealed class JSON : StringFormat {
    /**
     * Serializes and encodes the given [value] to JSON using the given [serializer].
     * @return [JSONObject], [JSONArray], [Boolean], [Number], [String], or [JSONObject.NULL]
     */
    fun <T> encodeToJSON(serializer: SerializationStrategy<T>, value: T): Any =
        JSONEncoder(serializersModule).apply { encodeSerializableValue(serializer, value) }.value

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        encodeToJSON(serializer, value).jsonToString()

    /**
     * Decodes and deserializes the given [json] to the value of type [T] using the given [deserializer].
     * @param json [JSONObject], [JSONArray], [Boolean], [Number], [String], or [JSONObject.NULL]
     */
    fun <T> decodeFromJSON(deserializer: DeserializationStrategy<T>, json: Any): T =
        JSONDecoder(serializersModule, json).decodeSerializableValue(deserializer)

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        decodeFromJSON(deserializer, JSONTokener(string).nextValue())

    class Builder(from: JSON = Default) {
        var serializersModule: SerializersModule = from.serializersModule

        fun build(): JSON = Impl(serializersModule)
    }

    private class Impl(override val serializersModule: SerializersModule) : JSON()

    companion object Default : JSON() {
        override val serializersModule: SerializersModule
            get() = EmptySerializersModule()
    }
}

inline fun JSON(from: JSON = JSON.Default, builderAction: JSON.Builder.() -> Unit = {}): JSON =
    JSON.Builder(from).apply(builderAction).build()

inline fun <reified T> JSON.encodeToJSON(value: T): Any = encodeToJSON(serializer(), value)
inline fun <reified T> JSON.encodeToString(value: T): String = encodeToString(serializer(), value)

inline fun <reified T> JSON.decodeFromJSON(json: Any): T = decodeFromJSON(serializer(), json)
inline fun <reified T> JSON.decodeFromString(json: String): T = decodeFromString(serializer(), json)

/** Simple re-implementation of [JSONObject.valueToString] which is not available on Android. */
private fun Any.jsonToString(): String = when (this) {
    is Number -> JSONObject.numberToString(this)
    is String -> JSONObject.quote(this)
    else -> toString()
}

private abstract class BaseJSONEncoder(override val serializersModule: SerializersModule) : AbstractEncoder() {
    abstract fun put(value: Any)

    override fun encodeValue(value: Any) {
        put(value)
    }

    override fun encodeNull() {
        put(JSONObject.NULL)
    }

    override fun encodeLong(value: Long) {
        if (value in (-1L shl 53)..(1L shl 53)) super.encodeLong(value) else encodeString(value.toString())
    }

    override fun encodeFloat(value: Float) {
        if (value.isFinite()) super.encodeFloat(value) else encodeString(value.toString())
    }

    override fun encodeDouble(value: Double) {
        if (value.isFinite()) super.encodeDouble(value) else encodeString(value.toString())
    }

    override fun encodeChar(value: Char) {
        encodeString(value.toString())
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encodeString(enumDescriptor.getElementName(index))
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> JSONListEncoder(serializersModule).also { put(it.value) }
            StructureKind.MAP -> JSONMapEncoder(serializersModule).also { put(it.value) }
            else -> JSONObjectEncoder(serializersModule).also { put(it.value) }
        }
}

private class JSONEncoder(serializersModule: SerializersModule) : BaseJSONEncoder(serializersModule) {
    lateinit var value: Any
        private set

    override fun put(value: Any) {
        this.value = value
    }
}

private class JSONListEncoder(serializersModule: SerializersModule) : BaseJSONEncoder(serializersModule) {
    val value = JSONArray()

    override fun put(value: Any) {
        this.value.put(value)
    }
}

private class JSONMapEncoder(serializersModule: SerializersModule) : BaseJSONEncoder(serializersModule) {
    val value = JSONObject()
    private var key: String? = null

    override fun put(value: Any) {
        key = when (val key = key) {
            null -> value.toString()
            else -> {
                this.value.put(key, value)
                null
            }
        }
    }
}

private class JSONObjectEncoder(serializersModule: SerializersModule) : BaseJSONEncoder(serializersModule) {
    val value = JSONObject()
    private lateinit var key: String

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        key = descriptor.getElementName(index)
        return true
    }

    override fun put(value: Any) {
        this.value.put(key, value)
    }
}

private inline fun <reified T : Any> typeMismatch(value: Any?): T {
    throw SerializationException(
        if (value == null) {
            "Value is null."
        } else {
            "Value $value of type ${value::class.java} cannot be converted to ${T::class.java}"
        }
    )
}

private abstract class BaseJSONDecoder(override val serializersModule: SerializersModule) : AbstractDecoder() {
    abstract override fun decodeBoolean(): Boolean
    override fun decodeByte(): Byte = decodeInt().toByte()
    override fun decodeShort(): Short = decodeInt().toShort()
    abstract override fun decodeInt(): Int
    abstract override fun decodeLong(): Long
    override fun decodeFloat(): Float = decodeDouble().toFloat()
    abstract override fun decodeDouble(): Double
    override fun decodeChar(): Char = decodeString().single()
    abstract override fun decodeString(): String
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = enumDescriptor.getElementIndex(decodeString())

    protected abstract fun decodeArray(): JSONArray
    protected abstract fun decodeObject(): JSONObject

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = when (descriptor.kind) {
        is StructureKind.LIST -> JSONListDecoder(serializersModule, decodeArray())
        is StructureKind.MAP -> JSONMapDecoder(serializersModule, decodeObject())
        else -> JSONObjectDecoder(serializersModule, decodeObject())
    }
}

private class JSONDecoder(
    serializersModule: SerializersModule,
    private val json: Any,
) : BaseJSONDecoder(serializersModule) {
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        throw SerializationException("Missing beginStructure().")
    }

    private inline fun <reified T, reified R> decode(decodeValue: (T) -> R, decodeString: (String) -> R): R =
        when (json) {
            is T -> decodeValue(json)
            is String -> decodeString(json)
            else -> typeMismatch(json)
        }

    override fun decodeNotNullMark(): Boolean = json != JSONObject.NULL
    override fun decodeBoolean(): Boolean = decode({ value: Boolean -> value }, String::toBoolean)
    override fun decodeByte(): Byte = decode(Number::toByte, String::toByte)
    override fun decodeShort(): Short = decode(Number::toShort, String::toShort)
    override fun decodeInt(): Int = decode(Number::toInt, String::toInt)
    override fun decodeLong(): Long = decode(Number::toLong, String::toLong)
    override fun decodeFloat(): Float = decode(Number::toFloat, String::toFloat)
    override fun decodeDouble(): Double = decode(Number::toDouble, String::toDouble)
    override fun decodeString(): String = json.toString()

    override fun decodeArray(): JSONArray = json as? JSONArray ?: typeMismatch(json)
    override fun decodeObject(): JSONObject = json as? JSONObject ?: typeMismatch(json)
}

private class JSONListDecoder(
    serializersModule: SerializersModule,
    private val json: JSONArray,
) : BaseJSONDecoder(serializersModule) {
    private var index = 0

    override fun decodeSequentially(): Boolean = true
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = json.length()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < json.length()) index else CompositeDecoder.DECODE_DONE

    override fun decodeNotNullMark(): Boolean = json.get(index) != JSONObject.NULL
    override fun decodeNull(): Nothing? = null.also { index++ }
    override fun decodeBoolean(): Boolean = json.getBoolean(index).also { index++ }
    override fun decodeInt(): Int = json.getInt(index).also { index++ }
    override fun decodeLong(): Long = json.getLong(index).also { index++ }
    override fun decodeDouble(): Double = json.getDouble(index).also { index++ }
    override fun decodeString(): String = json.get(index).toString().also { index++ }

    override fun decodeArray(): JSONArray = json.getJSONArray(index).also { index++ }
    override fun decodeObject(): JSONObject = json.getJSONObject(index).also { index++ }
}

private class JSONMapDecoder(
    serializersModule: SerializersModule,
    private val json: JSONObject,
) : BaseJSONDecoder(serializersModule) {
    private val names = json.names()
    private var index = 0

    override fun decodeSequentially(): Boolean = true
    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = json.length()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < 2 * json.length()) index else CompositeDecoder.DECODE_DONE

    private inline fun <T> decode(decodeKey: JSONArray.(Int) -> T, decodeValue: JSONObject.(String) -> T): T =
        if (index % 2 == 0) decodeKey(names!!, index / 2) else decodeValue(json, names!!.getString(index / 2))

    override fun decodeNotNullMark(): Boolean = decode(JSONArray::get, JSONObject::get) != JSONObject.NULL
    override fun decodeNull(): Nothing? = null.also { index++ }
    override fun decodeBoolean(): Boolean = decode(JSONArray::getBoolean, JSONObject::getBoolean).also { index++ }
    override fun decodeInt(): Int = decode(JSONArray::getInt, JSONObject::getInt).also { index++ }
    override fun decodeLong(): Long = decode(JSONArray::getLong, JSONObject::getLong).also { index++ }
    override fun decodeDouble(): Double = decode(JSONArray::getDouble, JSONObject::getDouble).also { index++ }
    override fun decodeString(): String = decode(JSONArray::get, JSONObject::get).toString().also { index++ }

    override fun decodeArray(): JSONArray =
        decode(JSONArray::getJSONArray, JSONObject::getJSONArray).also { index++ }
    override fun decodeObject(): JSONObject =
        decode(JSONArray::getJSONObject, JSONObject::getJSONObject).also { index++ }
}

private class JSONObjectDecoder(
    serializersModule: SerializersModule,
    private val json: JSONObject,
) : BaseJSONDecoder(serializersModule) {
    private val names = json.names()
    private var index = 0
    private lateinit var name: String

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (index < json.length()) {
            name = names!!.getString(index++)
            descriptor.getElementIndex(name)
        } else CompositeDecoder.DECODE_DONE

    override fun decodeNotNullMark(): Boolean = json.get(name) != JSONObject.NULL
    override fun decodeBoolean(): Boolean = json.getBoolean(name)
    override fun decodeInt(): Int = json.getInt(name)
    override fun decodeLong(): Long = json.getLong(name)
    override fun decodeDouble(): Double = json.getDouble(name)
    override fun decodeString(): String = json.get(name).toString()

    override fun decodeArray(): JSONArray = json.getJSONArray(name)
    override fun decodeObject(): JSONObject = json.getJSONObject(name)
}
