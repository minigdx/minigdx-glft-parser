plugins {
    id("com.github.minigdx.gradle.plugin.developer") version "1.0.0-alpha0"
    id("com.github.minigdx.gradle.plugin.developer.jvm") version "1.0.0-alpha0" apply false
    id("com.github.minigdx.gradle.plugin.developer.mpp") version "1.0.0-alpha0" apply false
}

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
