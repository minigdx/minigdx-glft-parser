plugins {
    id("com.github.minigdx.gradle.plugin.developer") version "DEV-SNAPSHOT"
    id("com.github.minigdx.gradle.plugin.developer.jvm") version "DEV-SNAPSHOT" apply false
    id("com.github.minigdx.gradle.plugin.developer.mpp") version "DEV-SNAPSHOT" apply false
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
