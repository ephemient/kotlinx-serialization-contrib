//[annotations](../../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.annotations](../index.md)/[GenerateEnumSerializer](index.md)

# GenerateEnumSerializer

[common]\
@[Target](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-target/index.html)(allowedTargets = [[AnnotationTarget.CLASS](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-annotation-target/-c-l-a-s-s/index.html), [AnnotationTarget.ANNOTATION_CLASS](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.annotation/-annotation-target/-a-n-n-o-t-a-t-i-o-n_-c-l-a-s-s/index.html)])

annotation class [GenerateEnumSerializer](index.md)(mode: [GenerateEnumSerializer.Mode](-mode/index.md), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

## Types

| Name | Summary |
|---|---|
| [Mode](-mode/index.md) | [common]<br>enum [Mode](-mode/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[GenerateEnumSerializer.Mode](-mode/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [mode](mode.md) | [common]<br>val [mode](mode.md): [GenerateEnumSerializer.Mode](-mode/index.md) |
| [name](name.md) | [common]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Defaults to the target class name + mode + "EnumSerializer". |
