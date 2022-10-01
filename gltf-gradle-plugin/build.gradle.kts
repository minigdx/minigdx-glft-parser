plugins {
    id("com.github.minigdx.gradle.plugin.developer.jvm")
    id("java-gradle-plugin")

    // Plugin publication plugin.
    id("com.gradle.plugin-publish") version "1.0.0"
}

dependencies {
    api(gradleApi())
    implementation(project(":gltf-parser"))

    testImplementation(gradleTestKit())
    testImplementation(libs.test.junit)
}
tasks {
    test {
        useJUnitPlatform()
    }
}

gradlePlugin {
    val gltfGradlePlugin by plugins.creating {
        this.id = "com.github.minigdx.gradle.plugin.gltf"
        this.implementationClass = "com.github.dwursteisen.gltf.GltfPlugin"
        // id is captured from java-gradle-plugin configuration
        this.displayName = "MiniGDX gltf parser plugin"
        this.description = "Configure gltf parser to transform gltf files into minigdx file format."
    }
}

pluginBundle {
    website = "https://github.com/minigdx/minigdx-glft-parser"
    vcsUrl = "https://github.com/minigdx/minigdx-glft-parser"

    tags = listOf("minigdx", "gltf", "kotlin")
}
