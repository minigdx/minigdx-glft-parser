package com.dwursteisen.gltf.parser

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.scene.SceneParser
import com.dwursteisen.gltf.parser.sprite.SpriteParser
import com.dwursteisen.gltf.parser.sprite.internal.AsepriteDataModel
import com.dwursteisen.minigdx.scene.api.Scene
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File

class Dependency(val file: File, val uri: String)

class Parser(private val input: File) {

    @ExperimentalStdlibApi
    @ExperimentalSerializationApi
    fun toProtobuf(output: File): List<Dependency> {
        val model = if (input.extension in GLTF_EXTENSIONS) {
            val gltf = GltfAsset.fromFile(input.absolutePath)
            SceneParser(input.absoluteFile, gltf).parse()
        } else {
            SpriteParser(input, asepriteJsonMapper(input)).parse()
        }
        val data = Scene.writeProtobuf(model)
        output.writeBytes(data)
        return dependencies(model, input)
    }

    @ExperimentalSerializationApi
    @ExperimentalStdlibApi
    fun toJson(output: File): List<Dependency> {
        val model = if (input.extension in GLTF_EXTENSIONS) {
            val gltf = GltfAsset.fromFile(input.absolutePath)
            SceneParser(input.absoluteFile, gltf).parse()
        } else {
            SpriteParser(input, asepriteJsonMapper(input)).parse()
        }
        val data = Scene.writeJson(model)
        output.writeBytes(data)
        return dependencies(model, input)
    }

    private fun dependencies(
        model: Scene,
        input: File
    ): List<Dependency> {
        val dependencies = model.materials.values.filter { m -> m.isExternal }
            .mapNotNull { m -> m.uri }
            .map { uri -> Dependency(input.parentFile.resolve(uri), uri) }
        return dependencies
    }

    private fun asepriteJsonMapper(resource: File) = ObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(resource, AsepriteDataModel::class.java)

    companion object {
        private val GLTF_EXTENSIONS = setOf("gltf", "glb")
    }
}
