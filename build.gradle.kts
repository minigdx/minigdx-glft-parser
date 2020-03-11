plugins {
    kotlin("jvm") version "1.3.70"
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
    apply { plugin("maven-publish") }

    group = "com.github.dwursteisen.collada"
    version = project.properties["version"] ?: "1.0-SNAPSHOT"

    if (version == "unspecified") {
        version = "1.0-SNAPSHOT"
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
