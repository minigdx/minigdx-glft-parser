package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.minigdx.scene.api.Scene
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File

class SceneParserTest {

    private val sources = listOf("camera", "lights", "mesh", "uv")

    @Test
    fun `parse | it parses all file tests`() {
        sources.flatMap {
            File(SceneParser::class.java.getResource("/$it").toURI())
                .walkBottomUp()
                .toList()
        }.filter { it.name.endsWith(".gltf") }
            .map {
                it.name to SceneParser(GltfAsset.fromFile(it.absolutePath)!!)
            }.forEach {
                try {
                    val protobuf = Scene.writeProtobuf(it.second.parse())
                    Scene.readProtobuf(protobuf)
                } catch (ex: Exception) {
                    fail("Impossible to parse the file ${it.first}", ex)
                }
            }
    }
}
