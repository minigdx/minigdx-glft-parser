import java.util.*

plugins {
    kotlin("jvm") version "1.3.70"
    id("com.gradle.plugin-publish") version "0.10.1"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5" apply (false)
}

repositories {
    maven {
        url = uri("https://dl.bintray.com/dwursteisen/minigdx")
    }
    mavenCentral()
    jcenter()
}

subprojects {
    repositories {
        maven {
            url = uri("https://dl.bintray.com/dwursteisen/minigdx")
        }
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        jcenter()
    }
    apply { plugin("maven-publish") }
    apply { plugin("com.jfrog.bintray") }

    group = "com.github.dwursteisen.collada"
    version = project.properties["version"] ?: "1.0-SNAPSHOT"

    if (version == "unspecified") {
        version = "1.0-SNAPSHOT"
    }


    val properties = Properties()
    if (rootProject.file("local.properties").exists()) {
        properties.load(rootProject.file("local.properties").inputStream())
    }

    val bintrayUser = if (project.hasProperty("bintray_user")) {
        project.property("bintray_user") as? String
    } else {
        System.getProperty("BINTRAY_USER")
    }

    val bintrayKey = if (project.hasProperty("bintray_key")) {
        project.property("bintray_key") as? String
    } else {
        System.getProperty("BINTRAY_KEY")
    }

    configure<com.jfrog.bintray.gradle.BintrayExtension> {
        user = properties.getProperty("bintray.user") ?: bintrayUser
        key = properties.getProperty("bintray.key") ?: bintrayKey
        publish = true
        if (name == "collada-api") {
            if (findProperty("currentOs") == "macOS") {
                setPublications("jvm", "js", "macosX64", "iosArm64", "iosX64", "metadata")
            } else if (findProperty("currentOs") == "Windows") {
                setPublications("mingwX64")
            } else if (findProperty("currentOs") == "Linux") {
                setPublications("linuxX64")
            }
        } else {
            if (findProperty("currentOs") == "Linux") {
                if (name == "collada-gradle-plugin") {
                    setPublications("collada-gradle-pluginPluginMarkerMaven", "pluginMaven")
                } else {
                    setPublications("maven")
                }
            }
        }
        pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
            repo = "minigdx"
            name = rootProject.name
            githubRepo = "dwursteisen/minigdx-glft-parser.git"
            vcsUrl = "https://github.com/dwursteisen/minigdx-glft-parser.git"
            description = rootProject.description
            setLabels("java")
            setLicenses("Apache-2.0")
            desc = description
            version(closureOf<com.jfrog.bintray.gradle.BintrayExtension.VersionConfig> {
                this.name = rootProject.version.toString()
                released = Date().toString()
            })
        })
    }

    tasks.named("bintrayUpload") {
        dependsOn(project.tasks["publishToMavenLocal"])
    }

    tasks.withType<com.jfrog.bintray.gradle.tasks.BintrayUploadTask> {
        doFirst {
            project.publishing.publications
                .filterIsInstance<MavenPublication>()
                .forEach { publication ->
                    val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
                    if (moduleFile.exists()) {
                        publication.artifact(object :
                            org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(moduleFile) {
                            override fun getDefaultExtension() = "module"
                        })
                    }
                }
        }
    }

    project.afterEvaluate {
        project.publishing.publications.forEach {
            println("Available publication: ${it.name}")
        }
    }
}
