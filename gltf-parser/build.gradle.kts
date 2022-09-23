@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.minigdx.jvm)
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
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.serialization.protobuf)

    implementation(project(":gltf-api"))

    implementation(libs.minigdx.gltf.loader)
    implementation(libs.minigdx.kotlin.math)

    implementation(libs.misc.jsoup)
    implementation(libs.misc.jmonkey)
    implementation(libs.misc.pngdecoder)
    implementation(libs.misc.jackson.databind)
    implementation(libs.misc.jackson.kotlin)

    testImplementation(libs.test.junit)
}
