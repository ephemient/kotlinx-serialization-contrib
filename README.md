# kotlinx.serialization additions

## GenerateEnumSerializer

This annotation and [KSP](https://github.com/google/ksp) processor will generate enum serializers which allow for pre-specified mappings on the enum name. For example,

```kotlin
@GenerateEnumSerializer(mode = Mode.SnakeCase, name = "MyEnumSerializer")
@Serializable(with = MyEnumSerializer::class)
enum class MyEnum {
    HelloWorld,
}
```

will result in an enum that will naturally serialize to/from `"hello_world"`.

To use, add the annotation and processor to `build.gradle`.

```groovy
plugins {
    id 'org.jetbrains.kotlin.jvm' version '1.7.20'
    id 'org.jetbrains.kotlin.plugin.serialization' version '1.7.20'
    id 'com.google.devtools.ksp' version '1.7.20-1.0.6'
}

repositories {
    mavenCentral()
    maven {
        url = uri('https://maven.pkg.github.com/ephemient/kotlinx-serialization-contrib')
        credentials {
            // GitHub username and access token here
        }
    }
}

dependencies {
    implementation 'com.github.ephemient.kotlinx-serialization-contrib:annotations:0.0.2-SNAPSHOT'
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.2'
    ksp 'com.github.ephemient.kotlinx-serialization-contrib:processor:0.0.2-SNAPSHOT'
}
```

## JSON-java

This is a [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) format which uses [JSON-java](https://github.com/stleary/JSON-java) instead of `kotlinx.serialization.json`. Usage is similar to other formats.

```kotlin
val json = JSON.encodeToJSON(serializer, value)
val string = JSON.encodeToString(serializer, value)
val value = JSON.decodeFromJSON(serializer, json)
val value = JSON.decodeFromString(serializer, string)
```

The format may be customized using the builder interface.

```kotlin
JSON {
    serializersModule = SerializersModule {
        // ...
    }
}
```

This can be found in the `json-java` artifact.

```groovy
repositories {
    mavenCentral()
    maven {
        url = uri('https://maven.pkg.github.com/ephemient/kotlinx-serialization-contrib')
        credentials {
            // GitHub username and access token here
        }
    }
}

dependencies {
    implementation 'com.github.ephemient.kotlinx-serialization-contrib:json-java:0.0.2-SNAPSHOT'
}
```
