package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
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
                    it.second.parse()
                } catch (ex: Exception) {
                    fail("Impossible to parse the file ${it.first}", ex)
                }
            }
    }
}
