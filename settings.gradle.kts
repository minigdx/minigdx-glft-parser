rootProject.name = "minigdx-gltf-parser"

include(":gltf-parser")
include(":gltf-gradle-plugin")
include(":gltf-api")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}
