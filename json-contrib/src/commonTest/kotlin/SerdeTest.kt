package com.github.ephemient.kotlinx.serialization.contrib.json

import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

@Serializable
sealed class EnumVariant {
    abstract val name: String
}

@Serializable
data class Span(@SerialName("file_id") val fileId: Int, val start: Int, val end: Int)

@Serializable
@SerialName("Untyped")
data class UntypedEnumVariant(override val name: String, val span: Span) : EnumVariant()

@Serializable
@SerialName("WithValue")
data class WithValueEnumVariant(override val name: String, val span: Span) : EnumVariant()

@Serializable
@SerialName("StructLike")
data class StructLikeEnumVariant(override val name: String, val span: Span) : EnumVariant()

@Serializable
@SerialName("Typed")
data class TypedEnumVariant(override val name: String, val span: Span) : EnumVariant()

@ExperimentalSerializationApi
class SerdeTest {
    @Test
    fun test() {
        val serde = Serde()

        val data = """
            [
                {
                    "Untyped": ["hi", { "file_id": 1, "start": 2, "end": 3 }]
                },
                {
                    "WithValue": ["hi", { "file_id": 4, "start": 5, "end": 6 }]
                },
                {
                    "StructLike": ["hi", { "file_id": 7, "start": 8, "end": 9 }]
                },
                {
                    "Typed": ["hi", { "file_id": 10, "start": 11, "end": 12 }]
                }
            ]
        """.trimIndent()

        println(serde.decodeFromString<List<EnumVariant>>(data))
        println(
            serde.encodeToString(
                listOf(
                    UntypedEnumVariant("hi", Span(1, 2, 3)),
                    WithValueEnumVariant("hi", Span(4, 5, 6)),
                    StructLikeEnumVariant("hi", Span(7, 8, 9)),
                    TypedEnumVariant("hi", Span(10, 11, 12)),
                )
            )
        )
    }
}
