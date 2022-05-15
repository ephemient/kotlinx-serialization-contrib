//[json-java](../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.jsonjava](index.md)

# Package com.github.ephemient.kotlinx.serialization.contrib.jsonjava

## Types

| Name | Summary |
|---|---|
| JSON | [android, jvm]<br>[android]<br>sealed class [JSON]([android]-j-s-o-n/index.md) : StringFormat<br>[jvm]<br>sealed class [JSON]([jvm]-j-s-o-n/index.md) : StringFormat |

## Functions

| Name | Summary |
|---|---|
| decodeFromJSON | [android, jvm]<br>[android]<br>inline fun &lt;[T]([android]decode-from-j-s-o-n.md)&gt; [JSON]([android]-j-s-o-n/index.md).[decodeFromJSON]([android]decode-from-j-s-o-n.md)(json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T]([android]decode-from-j-s-o-n.md)<br>[jvm]<br>inline fun &lt;[T]([jvm]decode-from-j-s-o-n.md)&gt; [JSON]([jvm]-j-s-o-n/index.md).[decodeFromJSON]([jvm]decode-from-j-s-o-n.md)(json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T]([jvm]decode-from-j-s-o-n.md) |
| decodeFromString | [android, jvm]<br>[android]<br>inline fun &lt;[T]([android]decode-from-string.md)&gt; [JSON]([android]-j-s-o-n/index.md).[decodeFromString]([android]decode-from-string.md)(json: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T]([android]decode-from-string.md)<br>[jvm]<br>inline fun &lt;[T]([jvm]decode-from-string.md)&gt; [JSON]([jvm]-j-s-o-n/index.md).[decodeFromString]([jvm]decode-from-string.md)(json: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T]([jvm]decode-from-string.md) |
| encodeToJSON | [android, jvm]<br>[android]<br>inline fun &lt;[T]([android]encode-to-j-s-o-n.md)&gt; [JSON]([android]-j-s-o-n/index.md).[encodeToJSON]([android]encode-to-j-s-o-n.md)(value: [T]([android]encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>[jvm]<br>inline fun &lt;[T]([jvm]encode-to-j-s-o-n.md)&gt; [JSON]([jvm]-j-s-o-n/index.md).[encodeToJSON]([jvm]encode-to-j-s-o-n.md)(value: [T]([jvm]encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| encodeToString | [android, jvm]<br>[android]<br>inline fun &lt;[T]([android]encode-to-string.md)&gt; [JSON]([android]-j-s-o-n/index.md).[encodeToString]([android]encode-to-string.md)(value: [T]([android]encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>[jvm]<br>inline fun &lt;[T]([jvm]encode-to-string.md)&gt; [JSON]([jvm]-j-s-o-n/index.md).[encodeToString]([jvm]encode-to-string.md)(value: [T]([jvm]encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| JSON | [android, jvm]<br>[android]<br>inline fun [JSON]([android]-j-s-o-n.md)(from: [JSON]([android]-j-s-o-n/index.md) = JSON.Default, builderAction: [JSON.Builder]([android]-j-s-o-n/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): [JSON]([android]-j-s-o-n/index.md)<br>[jvm]<br>inline fun [JSON]([jvm]-j-s-o-n.md)(from: [JSON]([jvm]-j-s-o-n/index.md) = JSON.Default, builderAction: [JSON.Builder]([jvm]-j-s-o-n/-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) = {}): [JSON]([jvm]-j-s-o-n/index.md) |
