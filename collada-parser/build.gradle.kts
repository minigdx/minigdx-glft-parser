plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.3.70"
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
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
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0-1.3.70-eap-274-2")
    implementation(project(":collada-api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jsoup:jsoup:1.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
