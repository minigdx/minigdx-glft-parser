package com.github.dwursteisen.gltf

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property

open class GltfExtensions(val name: String, project: Project) {
    val gltfDirectory: DirectoryProperty = project.objects.directoryProperty()
    val gltfFiles: FileCollection = gltfDirectory.asFileTree.matching { it.include("**/*.gltf", "**/*.json") }

    val target: DirectoryProperty = project.objects.directoryProperty()
    val format: Property<Format> = project.objects.property(Format::class.java).value(Format.PROTOBUF)
}
