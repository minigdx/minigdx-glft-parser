package collada

import org.gradle.api.Plugin
import org.gradle.api.Project

class ColladaPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val container = project.container(ColladaExtensions::class.java) { name ->
            ColladaExtensions(name, project)
        }
        project.extensions.add("collada", container)

        val parentTask = project.tasks.create("collada")

        container.all { ext ->
            val taskName = ext.name + "Collada"
            val newTask = project.tasks.register(taskName, ColladaTask::class.java) { task ->
                task.daeDirectory.set(ext.daeDirectory)
                task.daeFiles.set(ext.daeFiles)
                task.outputDirectory.set(ext.target)
            }
            parentTask.dependsOn += newTask
        }
    }
}
