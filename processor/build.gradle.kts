plugins {
    kotlin("jvm")
    kotlin("kapt")
}

kotlin {
    target {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

dependencies {
    implementation(projects.annotations)
    implementation(libs.auto.service.annotations)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ksp.api)

    kapt(libs.auto.service)
}
