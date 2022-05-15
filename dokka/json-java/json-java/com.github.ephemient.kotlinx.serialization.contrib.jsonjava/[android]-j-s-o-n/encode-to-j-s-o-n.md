//[json-java](../../../index.md)/[com.github.ephemient.kotlinx.serialization.contrib.jsonjava](../index.md)/[[android]JSON](index.md)/[encodeToJSON](encode-to-j-s-o-n.md)

# encodeToJSON

[android]\
fun &lt;[T](encode-to-j-s-o-n.md)&gt; [encodeToJSON](encode-to-j-s-o-n.md)(serializer: SerializationStrategy&lt;[T](encode-to-j-s-o-n.md)&gt;, value: [T](encode-to-j-s-o-n.md)): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)

Serializes and encodes the given [value](encode-to-j-s-o-n.md) to JSON using the given [serializer](encode-to-j-s-o-n.md).

#### Return

[JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html), [JSONArray](https://developer.android.com/reference/kotlin/org/json/JSONArray.html), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), or [JSONObject.NULL](https://developer.android.com/reference/kotlin/org/json/JSONObject.html#null)
