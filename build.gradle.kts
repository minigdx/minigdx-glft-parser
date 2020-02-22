plugins {
    kotlin("jvm") version "1.3.61"
    id("com.gradle.plugin-publish") version "0.10.1"
}



subprojects {
    repositories {
        mavenCentral()
    }
    apply { plugin("org.jetbrains.kotlin.jvm") }
    apply { plugin("maven-publish") }

    group = "com.github.dwursteisen.collada"
    version = "1.0-SNAPSHOT"
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
}
