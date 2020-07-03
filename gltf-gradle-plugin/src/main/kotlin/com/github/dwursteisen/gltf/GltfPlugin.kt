package com.github.dwursteisen.gltf

import org.gradle.api.Plugin
import org.gradle.api.Project

class GltfPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val container = project.container(GltfExtensions::class.java) { name ->
            GltfExtensions(name, project)
        }
        project.extensions.add("gltfPlugin", container)

        val parentTask = project.tasks.create("gltf").apply {
            group = "gltf"
        }

        container.all { ext ->
            val taskName = ext.name + "Gltf"
            val newTask = project.tasks.register(taskName, GltfTask::class.java) { task ->
                task.gltfDirectory.set(ext.gltfDirectory)
                task.gltfFiles.set(ext.gltfFiles)
                task.outputDirectory.set(ext.target)
                task.format.set(ext.format.get())
            }
            parentTask.dependsOn += newTask
        }
    }
}
