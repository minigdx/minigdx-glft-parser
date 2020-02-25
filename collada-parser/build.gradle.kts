plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.61"
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
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    implementation(project(":collada-api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jsoup:jsoup:1.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
