package collada

import kotlinx.serialization.ImplicitReflectionSerializer
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

open class ColladaTask : DefaultTask() {

    init {
        group = "collada"
        description = "Parse a collada file."
    }

    @InputFiles
    val daeFiles = project.createProperty<FileCollection>()

    @InputDirectory
    val daeDirectory = project.objects.directoryProperty()

    @OutputDirectory
    val outputDirectory = project.objects.directoryProperty()

    @ImplicitReflectionSerializer
    @TaskAction
    fun generate() {
        daeFiles.get().forEach {
            val outputFile = outputDirectory.get().file(it.nameWithoutExtension + ".3d")
            logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
            Converter(it).toProtobuf(outputFile.asFile)
        }
    }
}
