package collada

import mini.gdx.MiniGdxFile
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
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

    @TaskAction
    fun generate() {
        daeFiles.get().forEach {
            val outputFile = outputDirectory.get().file(it.nameWithoutExtension + ".3d")
            logger.info("Will generate ${outputFile.asFile.absoluteFile}…")
            Parser(MiniGdxFile(outputFile.asFile)).parse(it.absolutePath)
        }
    }
}
