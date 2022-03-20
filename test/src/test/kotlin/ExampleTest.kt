package com.github.ephemient.kotlinx.serialization.contrib.test

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExampleTest {
    @Test
    fun `test default serialization`() {
        Assertions.assertEquals("\"upper_camel_case\"", Json.encodeToString(Example.UpperCamelCase))
        Assertions.assertEquals("\"upper_snake_case\"", Json.encodeToString(Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(Example.Force))
    }

    @Test
    fun `test default deserialization`() {
        Assertions.assertEquals(Json.decodeFromString<Example>("\"upper_camel_case\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString<Example>("\"upper_snake_case\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString<Example>("\"FORCE\""), Example.Force)
    }

    @Test
    fun `test kebab case serialization`() {
        Assertions.assertEquals("\"upper-camel-case\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"upper-snake-case\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleKebabCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test kebab case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"upper-camel-case\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"upper-snake-case\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleKebabCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test lower camel case serialization`() {
        Assertions.assertEquals("\"upperCamelCase\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"upperSnakeCase\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerCamelCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test lower camel case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"upperCamelCase\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"upperSnakeCase\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCamelCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test lower case serialization`() {
        Assertions.assertEquals("\"uppercamelcase\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"uppersnakecase\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test lower case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"uppercamelcase\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"uppersnakecase\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test lower dot case serialization`() {
        Assertions.assertEquals("\"upper.camel.case\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"upper.snake.case\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleLowerDotCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test lower dot case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"upper.camel.case\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"upper.snake.case\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleLowerDotCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test snake case serialization`() {
        Assertions.assertEquals("\"upper_camel_case\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"upper_snake_case\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleSnakeCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test snake case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"upper_camel_case\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"upper_snake_case\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleSnakeCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test upper camel case serialization`() {
        Assertions.assertEquals("\"UpperCamelCase\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"UpperSnakeCase\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleUpperCamelCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test upper camel case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"UpperCamelCase\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"UpperSnakeCase\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperCamelCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }

    @Test
    fun `test upper snake case serialization`() {
        Assertions.assertEquals("\"UPPER_CAMEL_CASE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.UpperCamelCase))
        Assertions.assertEquals("\"UPPER_SNAKE_CASE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.UPPER_SNAKE_CASE))
        Assertions.assertEquals("\"FORCE\"", Json.encodeToString(ExampleUpperSnakeCaseEnumSerializer, Example.Force))
    }

    @Test
    fun `test upper snake case deserialization`() {
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"UPPER_CAMEL_CASE\""), Example.UpperCamelCase)
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"UPPER_SNAKE_CASE\""), Example.UPPER_SNAKE_CASE)
        Assertions.assertEquals(Json.decodeFromString(ExampleUpperSnakeCaseEnumSerializer, "\"FORCE\""), Example.Force)
    }
}
