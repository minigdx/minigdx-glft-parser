package collada

import com.dwursteisen.gltf.parser.Parser
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

open class GltfTask : DefaultTask() {

    init {
        group = "gltf"
        description = "Parse a collada file."
    }

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
        if (gltfFiles.isPresent) {
            gltfFiles.get().forEach {
                val outputFile = outputDirectory.get().file(it.nameWithoutExtension + format.get().exts)
                logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
                val result = when (format.get()) {
                    Format.JSON -> Parser(it).toJson(outputFile.asFile)
                    Format.PROTOBUF -> Parser(it).toProtobuf(outputFile.asFile)
                    null -> throw IllegalArgumentException("a valid format is expected on your task")
                }
            }
        }
    }
}
