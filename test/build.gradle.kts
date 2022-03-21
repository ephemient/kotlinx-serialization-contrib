plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                compileOnly(projects.annotations)
                implementation(libs.kotlinx.serialization)
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.serialization.json)
            }
        }

        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation(libs.junit.jupiter)
            }
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

dependencies {
    ksp(projects.processor)
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}
