plugins {
    id("com.github.minigdx.gradle.plugin.developer.jvm")
    id("java-gradle-plugin")

    // Plugin publication plugin.
    id("com.gradle.plugin-publish") version "0.13.0"
}

dependencies {
    api(gradleApi())
    implementation(project(":gltf-parser"))

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

gradlePlugin {
    val gltfGradlePlugin by plugins.creating {
        this.id = "com.github.minigdx.gradle.plugin.gltf"
        this.implementationClass = "com.github.dwursteisen.gltf.GltfPlugin"
    }
}

pluginBundle {
    website = "https://github.com/minigdx/minigdx-glft-parser"
    vcsUrl = "https://github.com/minigdx/minigdx-glft-parser"

    (plugins) {
        // first plugin
        "gltfGradlePlugin" {
            // id is captured from java-gradle-plugin configuration
            displayName = "MiniGDX gltf parser plugin"
            description = """Configure gltf parser to transform gltf files into minigdx file format."""
            tags = listOf("minigdx", "gltf", "kotlin")
        }
    }
}
