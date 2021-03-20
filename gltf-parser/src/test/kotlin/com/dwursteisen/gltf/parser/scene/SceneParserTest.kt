package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.combined
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.Scene
import com.dwursteisen.minigdx.scene.api.relation.ObjectType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File
import kotlin.test.assertNotNull

class SceneParserTest {

    private val sources = listOf("camera", "lights", "mesh", "uv", "joints", "empty")

    private val scene by gltf("/scene/camera_and_cube.gltf")

    private val sceneWithEmpty by gltf("/scene/hierarchy.gltf")

    private val linkedObjects by gltf("/scene/cube_linked_with_customer_properties.gltf")

    private val emptyWithCube by gltf("/scene/empty_parent_of_cube.gltf")

    private val camera by gltf("/camera/camera_default.gltf")

    private val animation by gltf("/joints/cube_joints_animated.gltf")

    private val lights by gltf("/lights/lights.gltf")

    @Test
    fun `parse - it parses all file tests`() {
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
    fun `parse - file is parsed correctly`() {
        val scene = SceneParser(scene).parse()
        // Check that there is one camera
        scene.perspectiveCameras.values.first()
        val cube = scene.models.values.first()

        val cubeTransformation = scene.children.first { it.reference == cube.id }.transformation
        assertMat4Equals(translation(Float3(0f, 0f, -5f)), Mat4.fromColumnMajor(*cubeTransformation.translation))
    }

    @Test
    fun `parse - file with linked and custom properties is parsed`() {
        val scene = SceneParser(linkedObjects).parse()

        // 3 cubes  (1 camera + 1 light but unsupported yet)
        assertEquals(5, scene.children.size)
        val models = scene.children.filter { it.type == ObjectType.MODEL }
        assertEquals(3, models.size)
        val modelReferences = models.map { model -> model.reference }.toSet()
        assertEquals(1, modelReferences.size)
        assertTrue(modelReferences.contains(scene.models.values.first().id))
    }

    @Test
    fun `parse - file with objects hierarchy and empty objects`() {
        val scene = SceneParser(sceneWithEmpty).parse()
        assertEquals(2, scene.children.first().children.size)
    }

    @Test
    fun `parse - file with empty parent of a cube`() {
        val scene = SceneParser(emptyWithCube).parse()
        val parentCube = scene.children.first { it.name.startsWith("Cube") }
        val empty = parentCube.children.first()
        val cube = empty.children.first()

        val positionParent = Mat4.fromColumnMajor(*parentCube.transformation.translation)
        val positionEmpty = positionParent * Mat4.fromColumnMajor(*empty.transformation.translation)
        val positionCube = positionEmpty * Mat4.fromColumnMajor(*cube.transformation.translation)

        assertMat4Equals(translation(Float3(1f, 0f, 0f)), positionParent)
        assertMat4Equals(translation(Float3(2f, 0f, 0f)), positionEmpty)
        assertMat4Equals(translation(Float3(4f, 0f, 0f)), positionCube)
    }

    @Test
    fun `parse - it parses cameras`() {
        val scene = SceneParser(camera).parse()
        val (perspective, ortho) = scene.children.filter { it.type == ObjectType.CAMERA }
        assertEquals("Perspective", perspective.name)
        assertMat4Equals(Mat4.identity(), perspective.transformation.combined)
        assertEquals("Orthographic", ortho.name)
        assertMat4Equals(Mat4.identity(), perspective.transformation.combined)
    }

    @Test
    fun `parse - it parses armature`() {
        val scene = SceneParser(animation).parse()
        val armature = scene.children.first() { it.type == ObjectType.ARMATURE }
        assertMat4Equals(translation(Float3(0f, 0f, 1f)), armature.transformation.combined)
    }

    @Test
    fun `parse - it parses lights`() {
        val scene = SceneParser(lights).parse()

        assertEquals(2, scene.pointLights.size)
        assertEquals(ObjectType.LIGHT, scene.children.first().type)
    }
}
