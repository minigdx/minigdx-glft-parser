package com.github.dwursteisen.gltf

import com.dwursteisen.gltf.parser.Merger
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class MergeTask : SourceTask() {

    @OutputFile
    val output = project.createProperty<File>()

    @ExperimentalStdlibApi
    @TaskAction
    fun merge() {
        Merger(source.files.toList()).mergeAll(output.get())
    }
}
