package collada

import org.gradle.api.Plugin
import org.gradle.api.Project

class ColladaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val container = project.container(ColladaExtensions::class.java) { name ->
            ColladaExtensions(name, project)
        }
        project.extensions.add("colladaPlugin", container)

        val parentTask = project.tasks.create("collada").apply {
            group = "collada"
        }

        container.all { ext ->
            val taskName = ext.name + "Collada"
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
