plugins {
    id("com.github.minigdx.gradle.plugin.developer.jvm")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0.1")

    implementation(project(":gltf-api"))

    implementation("com.github.minigdx:gltf-loader:DEV-SNAPSHOT")
    implementation("com.github.minigdx:kotlin-math-jvm:DEV-SNAPSHOT")

    implementation("org.jsoup:jsoup:1.12.2")
    implementation("org.jmonkeyengine:jme3-core:3.2.2-stable")
    implementation("org.l33tlabs.twl:pngdecoder:1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
