// See https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file#focus=Comments-27-5181027.0-0
// libs is wrongly red in IntelliJ. The suppress is here until the bug is fixed.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.minigdx.common)
    alias(libs.plugins.minigdx.jvm) apply false
    alias(libs.plugins.minigdx.mpp) apply false
}

allprojects {
    apply { plugin("com.github.minigdx.gradle.plugin.developer") }

    minigdxDeveloper {
        this.name.set("minigdx-gltf-parser")
        this.description.set("Parser and converter of gltf files into minigdx protobuf/json model.")
        this.projectUrl.set("https://github.com/minigdx/minigdx-glft-parser")
        this.licence {
            name.set("MIT Licence")
            url.set("https://github.com/minigdx/minigdx-glft-parser/blob/master/LICENSE")
        }
        developer {
            name.set("David Wursteisen")
            email.set("david.wursteisen+minigdx@gmail.com")
            url.set("https://github.com/dwursteisen")
        }
    }
}
