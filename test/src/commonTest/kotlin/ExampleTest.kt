package com.github.ephemient.kotlinx.serialization.contrib.test

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleTest {
    @Test
    fun testDefaultSerialization() {
        assertEquals("\"upper_camel_case\"", Json.encodeToString(Example.UpperCamelCase))
        assertEquals("\"upper_snake_case\"", Json.encodeToString(Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(Example.Force))
    }

    @Test
    fun testDefaultDeserialization() {
        assertEquals(Json.decodeFromString<Example>("\"upper_camel_case\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString<Example>("\"upper_snake_case\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString<Example>("\"FORCE\""), Example.Force)
    }

    @Test
    fun testKebabCaseSerialization() {
        assertEquals("\"upper-camel-case\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"upper-snake-case\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testKebabCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"upper-camel-case\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"upper-snake-case\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testLowerCamelCaseSerialization() {
        assertEquals("\"upperCamelCase\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"upperSnakeCase\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testLowerCamelCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"upperCamelCase\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"upperSnakeCase\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testLowerCaseSerialization() {
        assertEquals("\"uppercamelcase\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"uppersnakecase\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testLowerCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"uppercamelcase\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"uppersnakecase\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testLowerDotCaseSerialization() {
        assertEquals("\"upper.camel.case\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"upper.snake.case\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testLowerDotCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"upper.camel.case\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"upper.snake.case\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testSnakeCaseSerialization() {
        assertEquals("\"upper_camel_case\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"upper_snake_case\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testSnakeCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"upper_camel_case\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"upper_snake_case\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testUpperCamelCaseSerialization() {
        assertEquals("\"UpperCamelCase\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"UpperSnakeCase\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testUpperCamelCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"UpperCamelCase\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"UpperSnakeCase\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun testUpperSnakeCaseSerialization() {
        assertEquals("\"UPPER_CAMEL_CASE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.UpperCamelCase))
        assertEquals("\"UPPER_SNAKE_CASE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        assertEquals("\"FORCE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.Force))
    }

    @Test
    fun testUpperSnakeCaseDeserialization() {
        assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"UPPER_CAMEL_CASE\""), Example.UpperCamelCase)
        assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"UPPER_SNAKE_CASE\""), Example.UPPER_SNAKE_CASE)
        assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }
}
