plugins {
    kotlin("jvm") version "1.3.70"
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    maven {
        url = uri("https://dl.bintray.com/dwursteisen/minigdx")
    }
    mavenCentral()
    jcenter()
}

subprojects {
    repositories {
        maven {
            url = uri("https://dl.bintray.com/dwursteisen/minigdx")
        }
        mavenCentral()
        jcenter()
    }
    apply { plugin("maven-publish") }

    group = "com.github.dwursteisen.collada"
    version = project.properties["version"] ?: "1.0-SNAPSHOT"

    if (version == "unspecified") {
        version = "1.0-SNAPSHOT"
    }
}
