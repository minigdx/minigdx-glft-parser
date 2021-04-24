plugins {
    id("com.github.minigdx.gradle.plugin.developer.mpp")
    kotlin("plugin.serialization") version "1.4.20"
}

dependencies {
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0.1")
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.0.1")
    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-jvm:1.0.1")
    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.0.1")

    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core-js:1.0.1")
    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-js:1.0.1")
    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json-js:1.0.1")
}
