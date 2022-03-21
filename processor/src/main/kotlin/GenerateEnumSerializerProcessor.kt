package com.github.ephemient.kotlinx.serialization.contrib.processor

import com.github.ephemient.kotlinx.serialization.contrib.annotations.GenerateEnumSerializer
import com.google.auto.service.AutoService
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class GenerateEnumSerializerProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator

    @OptIn(ExperimentalSerializationApi::class, KotlinPoetKspPreview::class, KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (enum in resolver.getSymbolsWithAnnotation(GenerateEnumSerializer::class.qualifiedName!!, false)) {
            if (enum !is KSClassDeclaration || enum.classKind != ClassKind.ENUM_CLASS) continue
            val type = enum.asStarProjectedType().toTypeName()

            for (config in enum.getAnnotationsByType(GenerateEnumSerializer::class)) {
                val mode = config.mode
                val packageName = enum.packageName.asString()
                val serializerName = config.name.ifEmpty {
                    val baseName = enum.qualifiedName?.asString()?.removePrefix("${packageName}.")
                        ?: enum.simpleName.asString()
                    "$baseName${mode.name}EnumSerializer"
                }

                val entries = enum.declarations
                    .filterIsInstance<KSClassDeclaration>()
                    .filter { it.classKind == ClassKind.ENUM_ENTRY }
                    .toList()

                TypeSpec.objectBuilder(serializerName)
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
                            .addMember("%T::class", ClassName("kotlinx.serialization", "ExperimentalSerializationApi"))
                            .addMember("%T::class", ClassName("kotlinx.serialization", "InternalSerializationApi"))
                            .build()
                    )
                    .addSuperinterface(KSerializer::class.asClassName().parameterizedBy(type))
                    .addProperty(
                        PropertySpec.builder("descriptor", SerialDescriptor::class, KModifier.OVERRIDE)
                            .initializer(
                                buildCodeBlock {
                                    beginControlFlow(
                                        "%M(%S, %T)",
                                        MemberName("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
                                        serializerName, SerialKind.ENUM::class,
                                    )
                                    for (entry in entries) {
                                        addStatement(
                                            "%N(%S, %M(%S, %T))",
                                            "element",
                                            entry.getAnnotationsByType(SerialName::class)
                                                .firstNotNullOfOrNull { it.value.ifEmpty { null } }
                                                ?: mode(entry.simpleName.asString()),
                                            MemberName("kotlinx.serialization.descriptors", "buildSerialDescriptor"),
                                            entry.qualifiedName?.asString() ?: listOfNotNull(
                                                (enum.qualifiedName?.asString() ?: packageName).ifEmpty { null },
                                                entry.simpleName.asString(),
                                            ).joinToString("."),
                                            StructureKind.OBJECT::class,
                                        )
                                    }
                                    endControlFlow()
                                }
                            )
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("serialize")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("encoder", Encoder::class)
                            .addParameter("value", type)
                            .addCode(
                                buildCodeBlock {
                                    beginControlFlow("val %N = when (%N)", "index", "value")
                                    entries.forEachIndexed { i, entry ->
                                        addStatement("%T -> %L", entry.asStarProjectedType().toTypeName(), i)
                                    }
                                    endControlFlow()
                                    addStatement("%N.%N(%N, %N)", "encoder", "encodeEnum", "descriptor", "index")
                                }
                            )
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("deserialize")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("decoder", Decoder::class)
                            .returns(type)
                            .addCode(
                                buildCodeBlock {
                                    beginControlFlow(
                                        "return when (val %N = %N.%N(%N))",
                                        "index", "decoder", "decodeEnum", "descriptor",
                                    )
                                    entries.forEachIndexed { i, entry ->
                                        addStatement("%L -> %T", i, entry.asStarProjectedType().toTypeName())
                                    }
                                    addStatement(
                                        "else -> throw %T(%P)",
                                        SerializationException::class,
                                        "\$index is not a valid ${enum.simpleName.asString()}, must be one of " +
                                                entries.map { it.simpleName.asString() }
                                    )
                                    endControlFlow()
                                }
                            )
                            .build()
                    )
                    .build()
                    .let { FileSpec.builder(packageName, serializerName).addType(it).build() }
                    .writeTo(
                        codeGenerator,
                        Dependencies(aggregating = false, sources = listOfNotNull(enum.containingFile).toTypedArray()),
                    )
            }
        }
        return emptyList()
    }

    @AutoService(SymbolProcessorProvider::class)
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
            GenerateEnumSerializerProcessor(environment)
    }
}

@Suppress("RegExpRepeatedSpace")
private val WORD_BOUNDARY =
    """_|(?<=[\p{L}&&[^\p{Lu}]])(?=\p{Lu})|(?<=[\p{N}])(?=\p{L})|(?<=[\p{L}])(?=\p{N})""".toRegex()

private operator fun GenerateEnumSerializer.Mode.invoke(name: String): String = when (this) {
    GenerateEnumSerializer.Mode.KebabCase -> name.split(WORD_BOUNDARY).joinToString("-") { it.lowercase() }
    GenerateEnumSerializer.Mode.LowerCamelCase ->
        name.split(WORD_BOUNDARY).withIndex().joinToString("") { (i, word) ->
            val lowercaseWord = word.lowercase()
            if (i == 0) lowercaseWord else lowercaseWord.replaceFirstChar { it.titlecase() }
        }
    GenerateEnumSerializer.Mode.LowerCase -> name.split(WORD_BOUNDARY).joinToString("") { it.lowercase() }
    GenerateEnumSerializer.Mode.LowerDotCase -> name.split(WORD_BOUNDARY).joinToString(".") { it.lowercase() }
    GenerateEnumSerializer.Mode.SnakeCase -> name.split(WORD_BOUNDARY).joinToString("_") { it.lowercase() }
    GenerateEnumSerializer.Mode.UpperCamelCase ->
        name.split(WORD_BOUNDARY).joinToString("") { it.lowercase().replaceFirstChar { it.titlecase() } }
    GenerateEnumSerializer.Mode.UpperSnakeCase -> name.split(WORD_BOUNDARY).joinToString("_") { it.uppercase() }
}
