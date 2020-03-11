plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.70"
}


kotlin {

    jvm {
        this.compilations.getByName("main").kotlinOptions.jvmTarget = "1.8"
    }
    js {
        this.useCommonJs()
        this.nodejs
    }
    mingwX64() {
        binaries {
            staticLib {}
            sharedLib {}
        }
    }
    linuxX64() {
        binaries {
            staticLib {}
            sharedLib {}
        }
    }
    ios() {
        binaries {
            staticLib {}
            sharedLib {}
        }
    }
    macosX64() {
        binaries {
            staticLib {}
            sharedLib {}
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0-1.3.70-eap-274-2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-common:0.20.0-1.3.70-eap-274-2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0-1.3.70-eap-274-2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:0.20.0-1.3.70-eap-274-2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0-1.3.70-eap-274-2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-js:0.20.0-1.3.70-eap-274-2")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val macosX64Main by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val linuxX64Main by getting
        val mingwX64Main by getting
        val allNatives = listOf(
            macosX64Main,
            iosX64Main,
            iosArm64Main,
            linuxX64Main,
            mingwX64Main
        )

        allNatives.forEach {
            it.dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0-1.3.70-eap-274-2")
                api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf-native:0.20.0-1.3.70-eap-274-2")
            }
        }
    }
}

configurations {
    this.all {
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-js:1.3.70")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.70")
        resolutionStrategy.force("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.70")
    }
}
