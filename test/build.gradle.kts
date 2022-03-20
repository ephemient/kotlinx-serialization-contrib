plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
    target {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

dependencies {
    compileOnly(projects.annotations)
    implementation(libs.kotlinx.serialization)

    ksp(projects.processor)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlinx.serialization.json)
}

tasks.test {
    useJUnitPlatform()
}
