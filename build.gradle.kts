buildscript {
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.dokka.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.kotlin.serialization.gradle.plugin)
        classpath(libs.ksp.gradle.plugin)
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
