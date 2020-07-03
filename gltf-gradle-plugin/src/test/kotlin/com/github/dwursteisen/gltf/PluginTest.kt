package com.github.dwursteisen.gltf

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PluginTest {

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    lateinit var buildFile: File

    @Before
    fun setUp() {
        buildFile = temporaryFolder.newFile("build.gradle.kts")

        File("src/test/resources/cube.gltf").copyTo(temporaryFolder.newFolder("src", "main", "resources").resolve("cube.gltf"), true)

        buildFile.writeText(
            """
plugins {
    kotlin("jvm") version "1.3.70"
    id("com.github.dwursteisen.gltf") version "1.0-SNAPSHOT"
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
            .withProjectDir(temporaryFolder.root)
            .withArguments(":gltf")
            .withPluginClasspath()
            .build()

        assertEquals(result.task(":gltf")?.outcome, TaskOutcome.SUCCESS)
    }
}
