rootProject.name = "minigdx-gltf-parser"

include(":collada-parser")
include(":collada-gradle-plugin")
include(":collada-api")

pluginManagement {
    repositories {
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        gradlePluginPortal()
    }
}
