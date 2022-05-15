//[json-java](../../../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.jsonjava](../../index.md)/[[android]JSON](../index.md)/[Default](index.md)

# Default

[android]\
object [Default](index.md) : [JSON](../index.md)

## Functions

| Name | Summary |
|---|---|
| [decodeFromJSON](../decode-from-j-s-o-n.md) | [android]<br>fun &lt;[T](../decode-from-j-s-o-n.md)&gt; [decodeFromJSON](../decode-from-j-s-o-n.md)(deserializer: DeserializationStrategy&lt;[T](../decode-from-j-s-o-n.md)&gt;, json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](../decode-from-j-s-o-n.md)<br>Decodes and deserializes the given [json](../decode-from-j-s-o-n.md) to the value of type [T](../decode-from-j-s-o-n.md) using the given [deserializer](../decode-from-j-s-o-n.md). |
| [decodeFromString](../decode-from-string.md) | [android]<br>open override fun &lt;[T](../decode-from-string.md)&gt; [decodeFromString](../decode-from-string.md)(deserializer: DeserializationStrategy&lt;[T](../decode-from-string.md)&gt;, string: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [T](../decode-from-string.md) |
| [encodeToJSON](../encode-to-j-s-o-n.md) | [android]<br>fun &lt;[T](../encode-to-j-s-o-n.md)&gt; [encodeToJSON](../encode-to-j-s-o-n.md)(serializer: SerializationStrategy&lt;[T](../encode-to-j-s-o-n.md)&gt;, value: [T](../encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)<br>Serializes and encodes the given [value](../encode-to-j-s-o-n.md) to JSON using the given [serializer](../encode-to-j-s-o-n.md). |
| [encodeToString](../encode-to-string.md) | [android]<br>open override fun &lt;[T](../encode-to-string.md)&gt; [encodeToString](../encode-to-string.md)(serializer: SerializationStrategy&lt;[T](../encode-to-string.md)&gt;, value: [T](../encode-to-string.md)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [serializersModule](../serializers-module.md) | [android]<br>open override val [serializersModule](../serializers-module.md): SerializersModule |
