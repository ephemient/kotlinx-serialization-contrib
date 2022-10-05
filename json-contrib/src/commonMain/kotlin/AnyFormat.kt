package com.github.ephemient.kotlinx.serialization.contrib.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@ExperimentalSerializationApi
sealed class AnyFormat(
    val ignoreUnknownKeys: Boolean,
    val encodeDefaults: Boolean,
) : SerialFormat {

    fun <T> encode(serializer: KSerializer<T>, value: T): Any? {
        var result: Any? = null
        AnyEncoder { result = it }.encodeSerializableValue(serializer, value)
        return result
    }

    fun <T> decode(deserializer: DeserializationStrategy<T>, value: Any?): T =
        AnyDecoder(value).decodeSerializableValue(deserializer)

    private inner class AnyEncoder(private val callback: (Any?) -> Unit) : Encoder {
        override val serializersModule: SerializersModule get() = this@AnyFormat.serializersModule
        override fun encodeNull() = callback(null)
        override fun encodeBoolean(value: Boolean) = callback(value)
        override fun encodeByte(value: Byte) = callback(value)
        override fun encodeShort(value: Short) = callback(value)
        override fun encodeChar(value: Char) = callback(value)
        override fun encodeInt(value: Int) = callback(value)
        override fun encodeLong(value: Long) = callback(value)
        override fun encodeFloat(value: Float) = callback(value)
        override fun encodeDouble(value: Double) = callback(value)
        override fun encodeString(value: String) = callback(value)
        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) =
            callback(enumDescriptor.getElementName(index))
        override fun encodeInline(descriptor: SerialDescriptor): Encoder = this
        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = when (descriptor.kind) {
            StructureKind.LIST -> ListEncoder(callback)
            StructureKind.MAP -> MapEncoder(callback)
            else -> ClassEncoder(callback)
        }

        private fun encodeJsonElement(serializer: SerializationStrategy<*>, value: Any?): Boolean {
            when (serializer) {
                JsonPrimitive.serializer() -> callback(encodeJson(value as JsonPrimitive))
                JsonPrimitive.serializer().nullable -> callback((value as JsonPrimitive?)?.let { encodeJson(it) })
                JsonArray.serializer() -> callback(encodeJson(value as JsonArray))
                JsonArray.serializer().nullable -> callback((value as JsonArray?)?.let { encodeJson(it) })
                JsonObject.serializer() -> callback(encodeJson(value as JsonObject))
                JsonObject.serializer().nullable -> callback((value as JsonObject?)?.let { encodeJson(it) })
                JsonElement.serializer() -> callback(encodeJson(value as JsonElement))
                JsonElement.serializer().nullable -> callback((value as JsonElement?)?.let { encodeJson(it) })
                else -> return false
            }
            return true
        }

        override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
            if (!encodeJsonElement(serializer, value)) super.encodeSerializableValue(serializer, value)
        }

        override fun <T : Any> encodeNullableSerializableValue(serializer: SerializationStrategy<T>, value: T?) {
            if (!encodeJsonElement(serializer, value)) super.encodeNullableSerializableValue(serializer, value)
        }
    }

    private abstract inner class StructureEncoder : CompositeEncoder {
        protected abstract fun encodeValue(descriptor: SerialDescriptor, index: Int, value: Any?)
        override val serializersModule: SerializersModule get() = this@AnyFormat.serializersModule
        override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean = encodeDefaults
        override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) =
            encodeValue(descriptor, index, value)
        override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) =
            encodeValue(descriptor, index, value)
        override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) =
            encodeValue(descriptor, index, value)
        override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) =
            encodeValue(descriptor, index, value)
        override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) =
            encodeValue(descriptor, index, value)
        override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) =
            encodeValue(descriptor, index, value)
        override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) =
            encodeValue(descriptor, index, value)
        override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) =
            encodeValue(descriptor, index, value)
        override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) =
            encodeValue(descriptor, index, value)
        override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder =
            AnyEncoder { encodeValue(descriptor, index, it) }

        override fun <T> encodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T,
        ) = AnyEncoder { encodeValue(descriptor, index, it) }.encodeSerializableValue(serializer, value)

        override fun <T : Any> encodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            serializer: SerializationStrategy<T>,
            value: T?,
        ) = AnyEncoder { encodeValue(descriptor, index, it) }.encodeNullableSerializableValue(serializer, value)
    }

    private inner class ListEncoder(private val callback: (List<Any?>) -> Unit) : StructureEncoder() {
        private val builder = mutableListOf<Any?>()
        override fun encodeValue(descriptor: SerialDescriptor, index: Int, value: Any?) { builder.add(value) }
        override fun endStructure(descriptor: SerialDescriptor) = callback(builder.toList())
    }

    private inner class MapEncoder(private val callback: (Map<Any?, Any?>) -> Unit) : StructureEncoder() {
        private val builder = mutableMapOf<Any?, Any?>()
        private var key: Any? = null
        override fun encodeValue(descriptor: SerialDescriptor, index: Int, value: Any?) =
            if (index % 2 == 0) key = value else builder[key] = value
        override fun endStructure(descriptor: SerialDescriptor) = callback(builder.toMap())
    }

    private inner class ClassEncoder(private val callback: (Map<Any?, Any?>) -> Unit) : StructureEncoder() {
        private val builder = mutableMapOf<Any?, Any?>()
        override fun encodeValue(descriptor: SerialDescriptor, index: Int, value: Any?) {
            builder[descriptor.getElementName(index)] = value
        }
        override fun endStructure(descriptor: SerialDescriptor) = callback(builder.toMap())
    }

    private inner class AnyDecoder(val value: Any?) : Decoder {
        override val serializersModule: SerializersModule get() = this@AnyFormat.serializersModule
        override fun decodeNotNullMark(): Boolean = value != null
        override fun decodeNull(): Nothing? = null
        override fun decodeBoolean(): Boolean = value.toBoolean()
        override fun decodeByte(): Byte = (value as Number).toByte()
        override fun decodeShort(): Short = (value as Number).toShort()
        override fun decodeChar(): Char = value.toString().single()
        override fun decodeInt(): Int = (value as Number).toInt()
        override fun decodeLong(): Long = (value as Number).toLong()
        override fun decodeFloat(): Float = (value as Number).toFloat()
        override fun decodeDouble(): Double = (value as Number).toDouble()
        override fun decodeString(): String = value.toString()
        override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
            val index = enumDescriptor.getElementIndex(value.toString())
            return if (index == CompositeDecoder.UNKNOWN_NAME) (value as Number).toInt() else index
        }
        override fun decodeInline(descriptor: SerialDescriptor): Decoder = this
        override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = when (descriptor.kind) {
            StructureKind.LIST -> ListDecoder(value as List<*>)
            StructureKind.MAP -> MapDecoder(value as Map<*, *>)
            else -> ClassDecoder(descriptor, value as Map<*, *>)
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> decodeJsonElementOrSerializableValue(
            deserializer: DeserializationStrategy<T>,
            default: () -> T,
        ): T = when (deserializer) {
            JsonPrimitive.serializer() -> decodeJsonPrimitive(value) as T
            JsonPrimitive.serializer().nullable -> value?.let { decodeJsonPrimitive(it) } as T
            JsonArray.serializer() -> decodeJsonArray(value as List<*>) as T
            JsonArray.serializer().nullable -> value?.let { decodeJsonArray(it as List<*>) } as T
            JsonObject.serializer() -> decodeJsonObject(value as Map<*, *>) as T
            JsonObject.serializer().nullable -> value?.let { decodeJsonObject(it as Map<*, *>) } as T
            JsonElement.serializer() -> decodeJson(value) as T
            JsonElement.serializer().nullable -> value?.let { decodeJson(it) } as T
            else -> default()
        }

        override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
            decodeJsonElementOrSerializableValue(deserializer) { super.decodeSerializableValue(deserializer) }

        override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? =
            decodeJsonElementOrSerializableValue(deserializer) { super.decodeNullableSerializableValue(deserializer) }
    }

    private abstract inner class StructureDecoder : CompositeDecoder {
        protected abstract fun decodeValue(descriptor: SerialDescriptor, index: Int): Any?
        override val serializersModule: SerializersModule get() = this@AnyFormat.serializersModule
        override fun endStructure(descriptor: SerialDescriptor) {}
        override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
            decodeValue(descriptor, index).toBoolean()
        override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
            (decodeValue(descriptor, index) as Number).toByte()
        override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
            decodeValue(descriptor, index).toString().single()
        override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
            (decodeValue(descriptor, index) as Number).toShort()
        override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
            (decodeValue(descriptor, index) as Number).toInt()
        override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
            (decodeValue(descriptor, index) as Number).toLong()
        override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
            (decodeValue(descriptor, index) as Number).toFloat()
        override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
            (decodeValue(descriptor, index) as Number).toDouble()
        override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String =
            decodeValue(descriptor, index).toString()
        override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
            AnyDecoder(decodeValue(descriptor, index))

        override fun <T> decodeSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T>,
            previousValue: T?,
        ): T = AnyDecoder(decodeValue(descriptor, index)).decodeSerializableValue(deserializer)

        override fun <T : Any> decodeNullableSerializableElement(
            descriptor: SerialDescriptor,
            index: Int,
            deserializer: DeserializationStrategy<T?>,
            previousValue: T?,
        ): T? = AnyDecoder(decodeValue(descriptor, index)).decodeNullableSerializableValue(deserializer)
    }

    private inner class ListDecoder(private val list: List<*>) : StructureDecoder() {
        private var index = 0
        override fun decodeValue(descriptor: SerialDescriptor, index: Int): Any? = list[index]
        override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            if (index < list.size) index++ else CompositeDecoder.DECODE_DONE
    }

    private inner class MapDecoder(map: Map<*, *>) : StructureDecoder() {
        private val list = map.entries.flatMap { listOf(it.key, it.value) }
        private var index = 0
        override fun decodeValue(descriptor: SerialDescriptor, index: Int): Any? = list[index]
        override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            if (index < list.size) index++ else CompositeDecoder.DECODE_DONE
    }

    private inner class ClassDecoder(descriptor: SerialDescriptor, map: Map<*, *>) : StructureDecoder() {
        private val list = buildList {
            for (entry in map) {
                val key = entry.key.toString()
                val index = descriptor.getElementIndex(key)
                if (index == CompositeDecoder.UNKNOWN_NAME) {
                    if (ignoreUnknownKeys) continue
                    else throw SerializationException("Unknown key '$key'")
                } else add(IndexedValue(index, entry.value))
            }
        }
        private val indices = buildMap { list.forEachIndexed { i, (index, _) -> put(index, i) } }
        private var index = 0
        override fun decodeValue(descriptor: SerialDescriptor, index: Int): Any? = list[indices.getValue(index)].value
        override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            if (index < list.size) list[index++].index else CompositeDecoder.DECODE_DONE
    }

    class Builder(from: AnyFormat = Default) {
        var ignoreUnknownKeys: Boolean = from.ignoreUnknownKeys
        var encodeDefaults: Boolean = from.encodeDefaults
        var serializersModule: SerializersModule = from.serializersModule

        fun build(): AnyFormat = AnyFormat.Impl(
            ignoreUnknownKeys = ignoreUnknownKeys,
            encodeDefaults = encodeDefaults,
            serializersModule = serializersModule,
        )
    }

    private class Impl(
        ignoreUnknownKeys: Boolean,
        encodeDefaults: Boolean,
        override val serializersModule: SerializersModule,
    ) : AnyFormat(
        ignoreUnknownKeys = ignoreUnknownKeys,
        encodeDefaults = encodeDefaults,
    )

    companion object Default : AnyFormat(
        ignoreUnknownKeys = true,
        encodeDefaults = false,
    ) {
        override val serializersModule: SerializersModule get() = EmptySerializersModule()
    }
}

@ExperimentalSerializationApi
inline fun AnyFormat(from: AnyFormat = AnyFormat.Default, builder: AnyFormat.Builder.() -> Unit): AnyFormat =
    AnyFormat.Builder(from).apply(builder).build()

@ExperimentalSerializationApi
inline fun <reified T> AnyFormat.encode(value: T): Any? = encode(serializer(), value)

@ExperimentalSerializationApi
inline fun <reified T> AnyFormat.decode(value: Any?): T = decode(serializer(), value)

private val stringToBoolean = mapOf(
    "y" to true, "n" to false,
    "yes" to true, "no" to false,
    "true" to true, "false" to false,
    "on" to true, "off" to false,
)

private fun Any?.toBoolean(): Boolean = when (this) {
    is Boolean -> this
    is Number -> toString().any { it in '1'..'9' }
    is CharSequence -> toString().let { stringToBoolean[it.lowercase()] ?: it.any { it in '1'..'9' } }
    else -> this != null
}

private fun encodeJson(value: JsonPrimitive): Any? =
    if (value.isString) value.content
    else value.booleanOrNull ?: value.intOrNull ?: value.longOrNull ?: value.doubleOrNull
private fun encodeJson(value: JsonArray): List<Any?> = value.map { encodeJson(it) }
private fun encodeJson(value: JsonObject): Map<String, Any?> = value.mapValues { encodeJson(it.value) }
private fun encodeJson(value: JsonElement): Any? = when (value) {
    JsonNull -> null
    is JsonPrimitive -> encodeJson(value)
    is JsonArray -> encodeJson(value)
    is JsonObject -> encodeJson(value)
}

private fun decodeJsonPrimitive(value: Any?): JsonPrimitive = when (value) {
    null -> JsonNull
    is Boolean -> JsonPrimitive(value)
    is Number -> JsonPrimitive(value)
    is String -> JsonPrimitive(value)
    else -> throw SerializationException("not primitive")
}
private fun decodeJsonArray(value: Iterable<*>): JsonArray = buildJsonArray {
    for (element in value) add(decodeJson(element))
}
private fun decodeJsonObject(value: Map<*, *>): JsonObject = buildJsonObject {
    for (entry in value) put(entry.key.toString(), decodeJson(entry.value))
}
private fun decodeJson(value: Any?): JsonElement = when (value) {
    null, is Boolean, is Number, is String -> decodeJsonPrimitive(value)
    is Iterable<*> -> decodeJsonArray(value)
    is Map<*, *> -> decodeJsonObject(value)
    else -> throw SerializationException("not json")
}
