package com.dwursteisen.gltf.parser

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.scene.SceneParser
import com.dwursteisen.minigdx.scene.api.Scene
import java.io.File

class Parser(private val input: File) {

    fun toProtobuf(output: File) {
        val gltf = GltfAsset.fromFile(input.absolutePath)
            ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
        val model = SceneParser(gltf).parse()
        val data = Scene.writeProtobuf(model)
        output.writeBytes(data)
    }

    @ExperimentalStdlibApi
    fun toJson(output: File) {
        val gltf = GltfAsset.fromFile(input.absolutePath)
            ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
        val model = SceneParser(gltf).parse()
        val data = Scene.writeJson(model)
        output.writeBytes(data)
    }
}
