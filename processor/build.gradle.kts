plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.dokka")
    id("com.github.ephemient.kotlinx-serialization-contrib.build.publishing")
}

description = "GenerateEnumSerializer KSP processor"

kotlin {
}

dependencies {
    implementation(projects.annotations)
    implementation(libs.auto.service.annotations)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ksp.api)

    kapt(libs.auto.service)
}
