enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        val androidBuildToolsVersion: String by settings
        val kotlinVersion: String by settings

        resolutionStrategy {
            eachPlugin {
                val id = requested.id.id
                when {
                    id.startsWith("com.android.") -> useVersion(androidBuildToolsVersion)
                    id.startsWith("org.jetbrains.kotlin.") -> useVersion(kotlinVersion)
                }
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlinx-serialization-contrib"
include(":annotations", ":json-java", ":processor", ":test")
