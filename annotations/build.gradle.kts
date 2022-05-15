plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.github.ephemient.kotlinx-serialization-contrib.build.publishing")
}

description = "GenerateEnumSerializer annotation"

kotlin {
    jvm()
    js(IR) {
        nodejs()
    }
}
