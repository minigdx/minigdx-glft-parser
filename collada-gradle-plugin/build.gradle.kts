apply { plugin("java-gradle-plugin") }

dependencies {
    api(gradleApi())
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

