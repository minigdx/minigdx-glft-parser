plugins {
    `java-library`
    kotlin("jvm")
    id("java-gradle-plugin")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

dependencies {
    api(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":gltf-parser"))

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

configure<GradlePluginDevelopmentExtension> {
    this.plugins {
        create("gltf-gradle-plugin") {
            this.id = "com.github.dwursteisen.gltf"
            this.implementationClass = "com.github.dwursteisen.gltf.GltfPlugin"
        }
    }
}

