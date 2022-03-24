package com.github.ephemient.kotlinx.serialization.contrib.test

import com.github.ephemient.kotlinx.serialization.contrib.annotations.GenerateEnumSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.KebabCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.LowerCamelCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.LowerCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.LowerDotCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.SnakeCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.UpperCamelCase)
@GenerateEnumSerializer(mode = GenerateEnumSerializer.Mode.UpperSnakeCase)
@GenerateEnumSerializer(name = "ExampleSerializer")
@Serializable(with = ExampleSerializer::class)
enum class Example {
    UpperCamelCase,
    UPPER_SNAKE_CASE,
    @SerialName("FORCE")
    Force,
    ;

    override fun toString(): String = toSerialName()
}
