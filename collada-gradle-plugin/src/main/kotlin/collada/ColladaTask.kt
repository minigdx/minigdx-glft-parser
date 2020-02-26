package collada

import kotlinx.serialization.ImplicitReflectionSerializer
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

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

    @Input
    val format = project.createProperty<Format>()

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @TaskAction
    fun generate() {
        daeFiles.get().forEach {
            val outputFile = outputDirectory.get().file(it.nameWithoutExtension + format.get().exts)
            logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
            val result = when(format.get()) {
                Format.JSON -> Converter(it).toJson(outputFile.asFile)
                Format.PROTOBUF -> Converter(it).toProtobuf(outputFile.asFile)
                null -> throw IllegalArgumentException("a valid format is expected on your task")
            }
        }
    }
}
