plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("com.github.ephemient.kotlinx-serialization-contrib.build.publishing")
}

description = "JSON-java format for kotlinx.serialization"

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 14
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

kotlin {
    jvm()
    android {
        publishLibraryVariants("release")
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(libs.kotlinx.serialization)
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        getByName("jvmMain") {
            kotlin.srcDir("src/commonJvmMain/kotlin")
            dependencies {
                implementation(libs.org.json)
            }
        }

        getByName("androidMain") {
            kotlin.srcDir("src/commonJvmMain/kotlin")
        }

        getByName("jvmTest") {
            kotlin.srcDir("src/commonJvmTest/kotlin")
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation(libs.junit.jupiter)
            }
        }
    
        getByName("androidTest") {
            kotlin.srcDir("src/commonJvmTest/kotlin")
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.junit4)
                implementation(libs.robolectric)
            }
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}
