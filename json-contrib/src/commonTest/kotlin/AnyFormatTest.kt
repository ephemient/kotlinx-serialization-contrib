package com.github.ephemient.kotlinx.serialization.contrib.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

@ExperimentalSerializationApi
class AnyFormatTest {
    @Test
    fun encodeBuiltins() {
        assertEquals(42, AnyFormat.encode(42))
        assertEquals("Hello, world!", AnyFormat.encode("Hello, world!"))
        assertEquals(
            listOf(mapOf("first" to "a", "second" to 1.0), mapOf("first" to "b", "second" to 2.0)),
            AnyFormat.encode(listOf("a" to 1.0, "b" to 2.0))
        )
        assertEquals(
            mapOf("x" to true, "y" to false, "z" to null),
            AnyFormat.encode(mapOf("x" to true, "y" to false, "z" to null))
        )
    }

    @Test
    fun decodeBuiltins() {
        assertEquals(42, AnyFormat.decode(42))
        assertEquals("Hello, world!", AnyFormat.decode("Hello, world!"))
        assertEquals(
            listOf("a" to 1.0, "b" to 2.0),
            AnyFormat.decode(listOf(mapOf("first" to "a", "second" to 1.0), mapOf("first" to "b", "second" to 2.0)))
        )
        assertEquals(
            mapOf("x" to true, "y" to false, "z" to null),
            AnyFormat.decode(mapOf("x" to "1", "y" to "", "z" to null))
        )
    }

    @Test
    fun encodeJson() {
        assertNull(AnyFormat.encode<JsonPrimitive>(JsonNull))
        assertNull(AnyFormat.encode<JsonPrimitive?>(JsonNull))
        assertNull(AnyFormat.encode<JsonPrimitive?>(null))
        assertNull(AnyFormat.encode<JsonArray?>(null))
        assertNull(AnyFormat.encode<JsonObject?>(null))
        assertNull(AnyFormat.encode<JsonElement>(JsonNull))
        assertNull(AnyFormat.encode<JsonElement?>(JsonNull))
        assertNull(AnyFormat.encode<JsonElement?>(null))
        assertEquals(42, AnyFormat.encode(JsonPrimitive(42)))
        assertEquals(42, AnyFormat.encode<JsonPrimitive?>(JsonPrimitive(42)))
        assertEquals(42, AnyFormat.encode<JsonElement>(JsonPrimitive(42)))
        assertEquals(42, AnyFormat.encode<JsonElement?>(JsonPrimitive(42)))
        assertEquals("Hello, world!", AnyFormat.encode(JsonPrimitive("Hello, world!")))
        assertEquals("Hello, world!", AnyFormat.encode<JsonPrimitive?>(JsonPrimitive("Hello, world!")))
        assertEquals("Hello, world!", AnyFormat.encode<JsonElement>(JsonPrimitive("Hello, world!")))
        assertEquals("Hello, world!", AnyFormat.encode<JsonElement?>(JsonPrimitive("Hello, world!")))
        assertEquals(
            mapOf("a" to listOf(1.0), "b" to listOf(2.0)),
            AnyFormat.encode(
                buildJsonObject {
                    putJsonArray("a") { add(1.0) }
                    putJsonArray("b") { add(2.0) }
                }
            )
        )
        assertEquals(
            mapOf("a" to listOf(1.0), "b" to listOf(2.0)),
            AnyFormat.encode<JsonObject?>(
                buildJsonObject {
                    putJsonArray("a") { add(1.0) }
                    putJsonArray("b") { add(2.0) }
                }
            )
        )
        assertEquals(
            mapOf("a" to listOf(1.0), "b" to listOf(2.0)),
            AnyFormat.encode<JsonElement>(
                buildJsonObject {
                    putJsonArray("a") { add(1.0) }
                    putJsonArray("b") { add(2.0) }
                }
            )
        )
        assertEquals(
            mapOf("a" to listOf(1.0), "b" to listOf(2.0)),
            AnyFormat.encode<JsonElement?>(
                buildJsonObject {
                    putJsonArray("a") { add(1.0) }
                    putJsonArray("b") { add(2.0) }
                }
            )
        )
    }

    @Test
    fun decodeJson() {
        assertEquals(JsonNull, AnyFormat.decode<JsonPrimitive>(null))
        assertNull(AnyFormat.decode<JsonPrimitive?>(null))
        assertNull(AnyFormat.decode<JsonArray?>(null))
        assertNull(AnyFormat.decode<JsonObject?>(null))
        assertEquals(JsonNull, AnyFormat.decode<JsonElement>(null))
        assertNull(AnyFormat.decode<JsonElement?>(null))
        assertEquals(JsonPrimitive(42), AnyFormat.decode(42))
        assertEquals(JsonPrimitive(42), AnyFormat.decode<JsonPrimitive?>(42))
        assertEquals(JsonPrimitive(42), AnyFormat.decode<JsonElement>(42))
        assertEquals(JsonPrimitive(42), AnyFormat.decode<JsonElement?>(42))
        assertEquals(JsonPrimitive("Hello, world!"), AnyFormat.decode("Hello, world!"))
        assertEquals(JsonPrimitive("Hello, world!"), AnyFormat.decode<JsonPrimitive?>("Hello, world!"))
        assertEquals(JsonPrimitive("Hello, world!"), AnyFormat.decode<JsonElement>("Hello, world!"))
        assertEquals(JsonPrimitive("Hello, world!"), AnyFormat.decode<JsonElement?>("Hello, world!"))
        assertEquals(
            buildJsonObject {
                putJsonArray("a") { add(1.0) }
                putJsonArray("b") { add(2.0) }
            },
            AnyFormat.decode(mapOf("a" to listOf(1.0), "b" to listOf(2.0)))
        )
        assertEquals(
            buildJsonObject {
                putJsonArray("a") { add(1.0) }
                putJsonArray("b") { add(2.0) }
            },
            AnyFormat.decode<JsonObject?>(mapOf("a" to listOf(1.0), "b" to listOf(2.0)))
        )
        assertEquals(
            buildJsonObject {
                putJsonArray("a") { add(1.0) }
                putJsonArray("b") { add(2.0) }
            },
            AnyFormat.decode<JsonElement>(mapOf("a" to listOf(1.0), "b" to listOf(2.0)))
        )
        assertEquals(
            buildJsonObject {
                putJsonArray("a") { add(1.0) }
                putJsonArray("b") { add(2.0) }
            },
            AnyFormat.decode<JsonElement?>(mapOf("a" to listOf(1.0), "b" to listOf(2.0)))
        )
    }
}
