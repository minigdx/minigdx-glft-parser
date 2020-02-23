plugins {
    kotlin("jvm") version "1.3.61"
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    mavenCentral()
    jcenter()
}

subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
    apply { plugin("org.jetbrains.kotlin.jvm") }
    apply { plugin("maven-publish") }

    group = "com.github.dwursteisen.collada"
    version = project.properties["version"] ?: "1.0-SNAPSHOT"

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/dwursteisen/collada-parser")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
