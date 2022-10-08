package com.github.dwursteisen.gltf

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.writeText
import kotlin.test.assertTrue

class PluginTest {

    @TempDir
    lateinit var temporaryFolder: Path

    private fun copyFolder(src: Path, dest: Path) {
        Files.walk(src)
            .use { stream -> stream.forEach { source -> copy(source, dest.resolve(src.relativize(source))) } }
    }

    private fun copy(source: Path, dest: Path) {
        if (dest.notExists()) {
            dest.createDirectories()
        }
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING)
    }

    @BeforeEach
    fun setUp() {
        val buildFile = temporaryFolder.resolve("build.gradle.kts")

        copyFolder(
            src = File("src/test/resources/").toPath(),
            dest = temporaryFolder.resolve("src/main/resources/")
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

        Assertions.assertEquals(result.task(":gltf")?.outcome, TaskOutcome.SUCCESS)
        val textureCopied = temporaryFolder.resolve("build").toFile().listFiles()!!.any { file ->
            file.parentFile.resolve("textures/Untitled.png").exists()
        }
        assertTrue(textureCopied)
    }

    @Test
    fun runParserWithConfigurationCache() {
        // Run the build
        GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("--configuration-cache", ":gltf")
            .withProjectDir(temporaryFolder.toFile())
            .build()

        val buildResult = GradleRunner.create()
            .forwardOutput()
            .withPluginClasspath()
            .withArguments("--configuration-cache", ":gltf")
            .withProjectDir(temporaryFolder.toFile())
            .build()

        assertTrue(buildResult.output.contains("Reusing configuration cache."))
    }
}
