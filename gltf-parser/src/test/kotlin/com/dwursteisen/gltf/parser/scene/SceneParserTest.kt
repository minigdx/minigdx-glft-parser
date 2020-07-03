package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.Scene
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File
import kotlin.test.assertNotNull

class SceneParserTest {

    private val sources = listOf("camera", "lights", "mesh", "uv", "joints")

    private val scene by gltf("/scene/camera_and_cube.gltf")

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
                    val scene = Scene.readProtobuf(protobuf)
                    assertNotNull(scene.animations)
                } catch (ex: Exception) {
                    fail("Impossible to parse the file ${it.first}", ex)
                }
            }
    }

    @Test
    fun `parse | file is parsed correctly`() {
        val scene = SceneParser(scene).parse()
        val camera = scene.perspectiveCameras.values.first()
        val cube = scene.models.values.first()

        val cameraTransformation = Mat4.fromColumnMajor(*camera.transformation.matrix)
        assertMat4Equals(Mat4.identity(), cameraTransformation)

        val cubeTransformation = Mat4.fromColumnMajor(*cube.transformation.matrix)
        assertMat4Equals(translation(Float3(0f, 0f, -5f)), cubeTransformation)
    }
}
