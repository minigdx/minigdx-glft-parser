package com.github.dwursteisen.gltf

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import kotlin.io.path.writeText

class PluginTest {

    @TempDir
    lateinit var temporaryFolder: Path

    @BeforeEach
    fun setUp() {
        val buildFile = temporaryFolder.resolve("build.gradle.kts")

        File("src/test/resources/cube.gltf").copyTo(
            temporaryFolder.resolve("src/main/resources/cube.gltf").toFile(), true
        )

        buildFile.writeText(
            """
plugins {
    kotlin("jvm") version "1.6.21"
    id("com.github.minigdx.gradle.plugin.gltf") version "1.0-SNAPSHOT"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

gltfPlugin {
    create("assets") {
        this.gltfDirectory.set(project.file("src/main/resources/"))
        this.target.set(project.buildDir)
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
            """.trimIndent()
        )
    }

    @Test
    fun runParser() {
        val result = GradleRunner.create()
            .withProjectDir(temporaryFolder.toFile())
            .withArguments(":gltf")
            .withPluginClasspath()
            .build()

        assertEquals(result.task(":gltf")?.outcome, TaskOutcome.SUCCESS)
    }
}
