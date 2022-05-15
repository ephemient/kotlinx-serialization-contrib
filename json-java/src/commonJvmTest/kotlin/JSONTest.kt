package com.github.ephemient.kotlinx.serialization.contrib.jsonjava

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@AndroidRunWith(AndroidRobolectricTestRunner::class)
class JSONTest {
    private val json = JSON()

    @Test
    fun `primitive serialization`() {
        assertJSON((-128).toByte(), "-128")
        assertJSON(32767.toShort(), "32767")
        assertJSON(-33554432, "-33554432")
        assertJSON(281474976710655L, "281474976710655")
        assertJSON(3.14f, "3.14")
        assertJSON(3.14159, "3.14159")
        assertJSON('!', "\"!\"")
        assertJSON("foobar", "\"foobar\"")
        assertJSON<Unit?>(null, "null")
    }

    @Test
    fun `values not representable by JSON literals`() {
        assertJSON(9007199254740993L, "\"9007199254740993\"")

        assertJSON(Float.POSITIVE_INFINITY, "\"Infinity\"")
        assertJSON(Float.NEGATIVE_INFINITY, "\"-Infinity\"")
        assertEquals("NaN", json.encodeToJSON(Float.NaN))
        json.decodeFromJSON<Float>("NaN").let { assertTrue(it.isNaN(), "Expected $it.isNaN() to be true.") }

        assertJSON(Double.POSITIVE_INFINITY, "\"Infinity\"")
        assertJSON(Double.NEGATIVE_INFINITY, "\"-Infinity\"")
        assertEquals("NaN", json.encodeToJSON(Double.NaN))
        json.decodeFromJSON<Double>("NaN").let { assertTrue(it.isNaN(), "Expected $it.isNaN() to be true.") }
    }

    @Test
    fun `list serialization`() {
        assertJSON(emptyList<Unit>(), "[]")
        assertJSON(listOf(42, null, 100), "[42,null,100]")
    }

    @Test
    fun `map serialization`() {
        assertJSON(emptyMap<String, Unit>(), "{}")
        assertJSON(mapOf("a" to 42, "b" to null, "c" to 100), """{"a":42,"b":null,"c":100}""")
    }

    @Test
    fun `class serialization`() {
        assertJSON(Bar.OK(Foo.TEST), """{"value":"TEST"}""")
    }

    @Test
    fun `object serialization`() {
        assertJSON(Bar.NG, "{}")
    }

    @Test
    fun `polymorphic serialization`() {
        assertJSON(Bar.OK(Foo.TEST), """{"type":"ok","value":{"value":"TEST"}}""", Bar.serializer())
        assertJSON(Bar.NG, """{"type":"ng","value":{}}""", Bar.serializer())
    }

    private inline fun <reified T> assertJSON(value: T, string: String) {
        assertEquals(string, json.encodeToString(value), "Encoding $value")
        assertEquals(value, json.decodeFromString(string), "Decoding $string")
    }

    private inline fun <reified T> assertJSON(value: T, string: String, serializer: KSerializer<T> = serializer()) {
        assertEquals(string, json.encodeToString(serializer, value), "Encoding $value")
        assertEquals(value, json.decodeFromString(serializer, string), "Decoding $string")
    }
}

private enum class Foo {
    TEST,
}

@Serializable
private sealed class Bar {
    @Serializable
    @SerialName("ok")
    data class OK(val value: Foo) : Bar()
    @Serializable
    @SerialName("ng")
    object NG : Bar()
}
