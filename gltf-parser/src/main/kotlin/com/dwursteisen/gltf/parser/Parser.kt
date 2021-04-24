package com.dwursteisen.gltf.parser

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.scene.SceneParser
import com.dwursteisen.gltf.parser.sprite.SpriteParser
import com.dwursteisen.gltf.parser.sprite.internal.AsepriteDataModel
import com.dwursteisen.minigdx.scene.api.Scene
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File

class Parser(private val input: File) {

    @ExperimentalStdlibApi
    @ExperimentalSerializationApi
    fun toProtobuf(output: File) {
        val model = if (input.extension == "gltf") {
            val gltf = GltfAsset.fromFile(input.absolutePath)
                ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
            SceneParser(gltf).parse()
        } else {
            SpriteParser(input, asepriteJsonMapper(input)).parse()
        }
        val data = Scene.writeProtobuf(model)
        output.writeBytes(data)
    }

    @ExperimentalSerializationApi
    @ExperimentalStdlibApi
    fun toJson(output: File) {
        val model = if (input.extension == "gltf") {
            val gltf = GltfAsset.fromFile(input.absolutePath)
                ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
            SceneParser(gltf).parse()
        } else {
            SpriteParser(input, asepriteJsonMapper(input)).parse()
        }
        val data = Scene.writeJson(model)
        output.writeBytes(data)
    }

    private fun asepriteJsonMapper(resource: File) = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(resource, AsepriteDataModel::class.java)
}
