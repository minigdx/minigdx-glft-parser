plugins {
    id("com.github.minigdx.gradle.plugin.developer.jvm")
    id("java-gradle-plugin")
}

dependencies {
    api(gradleApi())
    implementation(project(":gltf-parser"))

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

gradlePlugin {
    val gltfGradlePlugin by plugins.creating {
        this.id = "com.github.minigdx.gltf"
        this.implementationClass = "com.github.dwursteisen.gltf.GltfPlugin"
    }
}

