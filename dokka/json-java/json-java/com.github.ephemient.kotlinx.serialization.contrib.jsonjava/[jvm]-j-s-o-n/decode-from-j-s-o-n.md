//[json-java](../../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.jsonjava](../index.md)/[[jvm]JSON](index.md)/[decodeFromJSON](decode-from-j-s-o-n.md)

# decodeFromJSON

[jvm]\
fun &lt;[T](decode-from-j-s-o-n.md)&gt; [decodeFromJSON](decode-from-j-s-o-n.md)(deserializer: DeserializationStrategy&lt;[T](decode-from-j-s-o-n.md)&gt;, json: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](decode-from-j-s-o-n.md)

Decodes and deserializes the given [json](decode-from-j-s-o-n.md) to the value of type [T](decode-from-j-s-o-n.md) using the given [deserializer](decode-from-j-s-o-n.md).

## Parameters

jvm

| | |
|---|---|
| json | [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html), [JSONArray](https://developer.android.com/reference/kotlin/org/json/JSONArray.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), or [JSONObject.NULL](https://developer.android.com/reference/kotlin/org/json/JSONObject.html#null) |
