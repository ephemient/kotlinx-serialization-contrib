//[json-java](../../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.jsonjava](../index.md)/[[jvm]JSON](index.md)

# JSON

[jvm]\
sealed class [JSON](index.md) : StringFormat

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>class [Builder](-builder/index.md)(from: [JSON](index.md)) |
| [Default](-default/index.md) | [jvm]<br>object [Default](-default/index.md) : [JSON](index.md) |

## Functions

| Name | Summary |
|---|---|
| [decodeFromJSON](decode-from-j-s-o-n.md) | [jvm]<br>fun &lt;[T](decode-from-j-s-o-n.md)&gt; [decodeFromJSON](decode-from-j-s-o-n.md)(deserializer: DeserializationStrategy&lt;[T](decode-from-j-s-o-n.md)&gt;, json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](decode-from-j-s-o-n.md)<br>Decodes and deserializes the given [json](decode-from-j-s-o-n.md) to the value of type [T](decode-from-j-s-o-n.md) using the given [deserializer](decode-from-j-s-o-n.md). |
| [decodeFromString](decode-from-string.md) | [jvm]<br>open override fun &lt;[T](decode-from-string.md)&gt; [decodeFromString](decode-from-string.md)(deserializer: DeserializationStrategy&lt;[T](decode-from-string.md)&gt;, string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](decode-from-string.md) |
| [encodeToJSON](encode-to-j-s-o-n.md) | [jvm]<br>fun &lt;[T](encode-to-j-s-o-n.md)&gt; [encodeToJSON](encode-to-j-s-o-n.md)(serializer: SerializationStrategy&lt;[T](encode-to-j-s-o-n.md)&gt;, value: [T](encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>Serializes and encodes the given [value](encode-to-j-s-o-n.md) to JSON using the given [serializer](encode-to-j-s-o-n.md). |
| [encodeToString](encode-to-string.md) | [jvm]<br>open override fun &lt;[T](encode-to-string.md)&gt; [encodeToString](encode-to-string.md)(serializer: SerializationStrategy&lt;[T](encode-to-string.md)&gt;, value: [T](encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [serializersModule](serializers-module.md) | [jvm]<br>open override val [serializersModule](serializers-module.md): SerializersModule |

## Inheritors

| Name |
|---|
| Default |

## Extensions

| Name | Summary |
|---|---|
| decodeFromJSON | [android, jvm]<br>[android]<br>inline fun &lt;[T](../[android]decode-from-j-s-o-n.md)&gt; [JSON](../[android]-j-s-o-n/index.md).[decodeFromJSON](../[android]decode-from-j-s-o-n.md)(json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](../[android]decode-from-j-s-o-n.md)<br>[jvm]<br>inline fun &lt;[T](../[jvm]decode-from-j-s-o-n.md)&gt; [JSON](index.md).[decodeFromJSON](../[jvm]decode-from-j-s-o-n.md)(json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](../[jvm]decode-from-j-s-o-n.md) |
| decodeFromString | [android, jvm]<br>[android]<br>inline fun &lt;[T](../[android]decode-from-string.md)&gt; [JSON](../[android]-j-s-o-n/index.md).[decodeFromString](../[android]decode-from-string.md)(json: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../[android]decode-from-string.md)<br>[jvm]<br>inline fun &lt;[T](../[jvm]decode-from-string.md)&gt; [JSON](index.md).[decodeFromString](../[jvm]decode-from-string.md)(json: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../[jvm]decode-from-string.md) |
| encodeToJSON | [android, jvm]<br>[android]<br>inline fun &lt;[T](../[android]encode-to-j-s-o-n.md)&gt; [JSON](../[android]-j-s-o-n/index.md).[encodeToJSON](../[android]encode-to-j-s-o-n.md)(value: [T](../[android]encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>[jvm]<br>inline fun &lt;[T](../[jvm]encode-to-j-s-o-n.md)&gt; [JSON](index.md).[encodeToJSON](../[jvm]encode-to-j-s-o-n.md)(value: [T](../[jvm]encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| encodeToString | [android, jvm]<br>[android]<br>inline fun &lt;[T](../[android]encode-to-string.md)&gt; [JSON](../[android]-j-s-o-n/index.md).[encodeToString](../[android]encode-to-string.md)(value: [T](../[android]encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>[jvm]<br>inline fun &lt;[T](../[jvm]encode-to-string.md)&gt; [JSON](index.md).[encodeToString](../[jvm]encode-to-string.md)(value: [T](../[jvm]encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
