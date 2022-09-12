plugins {
    id("com.github.minigdx.gradle.plugin.developer.mpp")
    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.3.2")
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.3.2")
    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-jvm:1.3.2")
    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")

    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-core-js:1.3.2")
    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-js:1.3.2")
    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-json-js:1.3.2")
}
