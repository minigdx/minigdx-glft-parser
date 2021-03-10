plugins {
    id("com.github.minigdx.gradle.plugin.developer.mpp")
    kotlin("plugin.serialization") version "1.3.70"
}

dependencies {
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
    this.commonMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-common:0.20.0")

    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    this.jvmMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:0.20.0")

    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0")
    this.jsMainApi("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-js:0.20.0")
}
