package com.github.ephemient.kotlinx.serialization.contrib.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
annotation class GenerateEnumSerializer(
    val mode: Mode = Mode.SnakeCase,
    /** Defaults to the target class name + mode + "EnumSerializer". */
    val name: String = "",
) {
    enum class Mode {
        /** kebab-case */
        KebabCase,
        /** lowerCase */
        LowerCamelCase,
        /** lowercase */
        LowerCase,
        /** lower.dot.case */
        LowerDotCase,
        /** snake_case */
        SnakeCase,
        /** UpperCamelCase */
        UpperCamelCase,
        /** UPPER_SNAKE_CASE */
        UpperSnakeCase,
    }
}
