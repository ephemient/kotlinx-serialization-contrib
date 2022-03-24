package com.github.ephemient.kotlinx.serialization.contrib.processor

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
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class EnumSerialNamesProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val codeGenerator = environment.codeGenerator

    @OptIn(KotlinPoetKspPreview::class, KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        for (enum in resolver.getSymbolsWithAnnotation(Serializable::class.qualifiedName!!, false)) {
            if (enum !is KSClassDeclaration || enum.classKind != ClassKind.ENUM_CLASS) continue
            val enumClass = enum.asStarProjectedType().toClassName()

            val entries = enum.declarations
                .filterIsInstance<KSClassDeclaration>()
                .filter { it.classKind == ClassKind.ENUM_ENTRY }
                .associate { entry ->
                    val name = entry.simpleName.asString()
                    val serialName = entry.getAnnotationsByType(SerialName::class)
                        .firstNotNullOfOrNull { it.value.ifEmpty { null } }
                    name to (serialName ?: name)
                }

            FileSpec.builder(enumClass.packageName, enumClass.simpleNames.joinToString("_", postfix = "SerialNames"))
                .addFunction(
                    FunSpec.builder("toSerialName")
                        .receiver(enumClass)
                        .returns(STRING)
                        .beginControlFlow("return when (this)")
                        .apply {
                            for ((name, serialName) in entries) {
                                addStatement("%T.%N -> %S", enumClass, name, serialName)
                            }
                        }
                        .endControlFlow()
                        .build()
                )
                .addFunction(
                    FunSpec.builder(enumClass.simpleNames.joinToString("", "fromSerialNameTo", "OrNull"))
                        .receiver(STRING)
                        .returns(enumClass.copy(nullable = true))
                        .beginControlFlow("return when (this)")
                        .apply {
                            for ((name, serialName) in entries) {
                                addStatement("%S -> %T.%N", serialName, enumClass, name)
                            }
                        }
                        .addStatement("else -> null")
                        .endControlFlow()
                        .build()
                )
                .build()
                .writeTo(codeGenerator, Dependencies(false, *listOfNotNull(enum.containingFile).toTypedArray()))
        }
        return emptyList()
    }

    @AutoService(SymbolProcessorProvider::class)
    class Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
            EnumSerialNamesProcessor(environment)
    }
}
