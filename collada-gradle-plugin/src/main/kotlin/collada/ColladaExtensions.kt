package collada

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection

open class ColladaExtensions(val name: String, project: Project) {
    val daeDirectory: DirectoryProperty = project.objects.directoryProperty()
    val daeFiles: FileCollection = daeDirectory.asFileTree.matching { it.include("**/*.dae") }
    val target: DirectoryProperty = project.objects.directoryProperty()
}
