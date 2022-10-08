package com.github.dwursteisen.gltf

import com.dwursteisen.gltf.parser.Parser
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

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
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
    @TaskAction
    fun generate() {
        if (gltfFiles.isPresent) {
            gltfFiles.get().forEach {
                val outputFile = outputDirectory.get().file(it.nameWithoutExtension + format.get().exts)
                logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
                val dependencies = when (format.get()) {
                    Format.JSON -> Parser(it).toJson(outputFile.asFile)
                    Format.PROTOBUF -> Parser(it).toProtobuf(outputFile.asFile)
                }
                dependencies.forEach { deps ->
                    logger.info("Copying ${deps.file.name} as dependency of  ${it.name}")
                    deps.file.copyTo(outputDirectory.get().asFile.resolve(deps.uri))
                }
            }
        }
    }
}
