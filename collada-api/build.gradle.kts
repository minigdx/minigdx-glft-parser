plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.61"
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
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.14.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
            }
        }

        js().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-js"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.14.0")
            }
        }

        js().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        macosX64().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            }
        }
        iosX64().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            }
        }
        iosArm64().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            }
        }
        linuxX64().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            }
        }
        mingwX64().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.14.0")
            }
        }
    }
}
