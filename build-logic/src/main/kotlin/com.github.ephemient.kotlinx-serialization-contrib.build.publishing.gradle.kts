import groovy.util.Node
import groovy.util.NodeList

plugins {
    `maven-publish`
}

publishing {
    publications {
        withType<MavenPublication>().configureEach {
            pom {
                url.set("https://github.com/ephemient/kotlinx-serialization-contrib")
                description.convention(project.description)
            }
        }

        (findByName("kotlinMultiplatform") as? MavenPublication ?: create<MavenPublication>("maven")).apply {
            plugins.withType(JavaPlugin::class) {
                extensions.configure<JavaPluginExtension>("java") {
                    withSourcesJar()
                }
                from(components["java"])
            }

            plugins.withId("org.jetbrains.dokka") {
                val dokkaJar by tasks.registering(Jar::class) {
                    description = "Assembles a jar archive containing the Dokka HTML."
                    group = BasePlugin.BUILD_GROUP
                    from(tasks.named("dokkaHtml"))
                    archiveClassifier.set("dokka")
                }
                tasks.named("assemble") { dependsOn(dokkaJar) }
                artifact(dokkaJar)
            }

            if (name == "kotlinMultiplatform") {
                afterEvaluate {
                    val jvm = findByName("jvm") as? MavenPublication ?: return@afterEvaluate
                    // Gradle/Kotlin metadata consumers will pick the right platform out of the box,
                    // but POM consumers won't; force them to JVM.
                    pom.withXml {
                        val root = asNode()
                        val dependencies = ((root["dependencies"] as NodeList).firstOrNull() as Node?)?.apply {
                            for (child in children().toList()) remove(child as Node)
                        } ?: root.appendNode("dependencies")
                        dependencies.appendNode("dependency").apply {
                            appendNode("groupId", jvm.groupId)
                            appendNode("artifactId", jvm.artifactId)
                            appendNode("version", jvm.version)
                            appendNode("scope", "compile")
                        }
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ephemient/kotlinx-serialization-contrib")
            credentials(PasswordCredentials::class)
        }
    }
}
