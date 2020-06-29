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
    implementation(project(":collada-parser"))

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}

configure<GradlePluginDevelopmentExtension> {
    this.plugins {
        create("collada-gradle-plugin") {
            this.id = "com.github.dwursteisen.collada"
            this.implementationClass = "collada.ColladaPlugin"
        }
    }
}

