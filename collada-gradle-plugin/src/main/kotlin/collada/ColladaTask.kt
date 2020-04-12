package collada

import kotlinx.serialization.ImplicitReflectionSerializer
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

enum class Format(val exts: String) {
    JSON(".json"),
    PROTOBUF(".protobuf")
}

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

    @InputFiles
    val gltfFiles = project.createProperty<FileCollection>()

    @InputDirectory
    val gltfDirectory = project.objects.directoryProperty()

    @Input
    val format = project.createProperty<Format>()

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @TaskAction
    fun generate() {
        if (daeFiles.isPresent) {
            daeFiles.get().forEach {
                val outputFile = outputDirectory.get().file(it.nameWithoutExtension + format.get().exts)
                logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
                val result = when (format.get()) {
                    Format.JSON -> ColladaToMiniGdx(it).toJson(outputFile.asFile)
                    Format.PROTOBUF -> ColladaToMiniGdx(it).toProtobuf(outputFile.asFile)
                    null -> throw IllegalArgumentException("a valid format is expected on your task")
                }
            }
        }

        if (gltfFiles.isPresent) {
            gltfFiles.get().forEach {
                val outputFile = outputDirectory.get().file(it.nameWithoutExtension + format.get().exts)
                logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
                val result = when (format.get()) {
                    Format.JSON -> GltfToMiniGdx(it).toJson(outputFile.asFile)
                    Format.PROTOBUF -> GltfToMiniGdx(it).toProtobuf(outputFile.asFile)
                    null -> throw IllegalArgumentException("a valid format is expected on your task")
                }
            }
        }
    }
}
