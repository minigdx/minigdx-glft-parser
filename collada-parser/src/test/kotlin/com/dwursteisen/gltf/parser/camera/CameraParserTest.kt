package com.dwursteisen.gltf.parser.camera

import collada.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfCamera
import com.adrienben.tools.gltf.models.GltfCameraType
import com.curiouscreature.kotlin.math.*
import org.junit.jupiter.api.Test
import java.io.File
import org.junit.jupiter.api.Assertions.*

interface Camera {
    val name: String
    val transformation: Transformation
}

data class PerspectiveCamera(
    override val name: String,
    val far: Float,
    val near: Float,
    val fov: Float,
    override val transformation: Transformation
) : Camera

data class OrthographicCamera(
    override val name: String,
    val far: Float,
    val near: Float,
    val scale: Float,
    override val transformation: Transformation
) : Camera

class CameraParser(private val source: GltfAsset) {

    private fun <T : Camera> GltfAsset.convertToCameras(
        type: GltfCameraType,
        factory: (name: String, camera: GltfCamera, transformation: Mat4) -> T
    ): List<T> {
        val cameras = this.nodes.filter { node ->
            val children = node.children ?: emptyList()
            children.any { it.camera != null && it.camera?.type == type }
        }

        return cameras
            .map { node ->
                val t = node.translation.let { Float3(it.x, it.y, it.z) }
                    .let { translation(it) }

                val r = node.rotation.let { Quaternion(it.i, it.j, it.k, it.a) }
                    .let { Mat4.from(it) }

                val s = node.scale.let { Float3(it.x, it.y, it.z) }
                    .let { scale(it) }

                // Compensation between OpenGL and blender reference.
                val compensation = rotation(Float3(0f, 1f, 0f), 90f) * rotation(Float3(1f, 0f, 0f), 90f)

                val transformation = transpose(t * r * s * compensation)

                val cam = node.children!!.first { it.camera != null }
                factory(node.name ?: "", cam.camera!!, transformation)
            }
    }

    fun orthographicCameras(): List<OrthographicCamera> {
        val factory = { name: String, camera: GltfCamera, transformation: Mat4 ->
            OrthographicCamera(
                name = name,
                far = camera.orthographic?.zFar ?: 0f,
                near = camera.orthographic?.zNear ?: 0f,
                scale = camera.orthographic?.xMag ?: 0f,
                transformation = Transformation(transformation.toFloatArray())
            )
        }
        return source.convertToCameras(
            GltfCameraType.ORTHOGRAPHIC,
            factory
        )
    }

    fun perspectiveCameras(): List<PerspectiveCamera> {
        val factory = { name: String, camera: GltfCamera, transformation: Mat4 ->
            PerspectiveCamera(
                name = name,
                far = camera.perspective?.zFar ?: 0f,
                near = camera.perspective?.zNear ?: 0f,
                fov = camera.perspective?.yFov ?: 90f,
                transformation = Transformation(transformation.toFloatArray())
            )
        }
        return source.convertToCameras(
            GltfCameraType.PERSPECTIVE,
            factory
        )
    }
}

class CameraParserTest {

    private val defaultCameras = "/camera/camera_default.gltf"

    @Test
    fun `parse | it parses orthographic camera`() {
        val gltf = GltfAsset.fromFile(defaultCameras.asFile().absolutePath)!!
        val cameras = CameraParser(gltf).orthographicCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.first()
        assertEquals("Orthographic", camera.name)
        assertEquals(Mat4.identity(), Mat4.of(*camera.transformation.matrix))
    }

    @Test
    fun `parse | it parses perspective camera`() {
        val gltf = GltfAsset.fromFile(defaultCameras.asFile().absolutePath)!!
        val cameras = CameraParser(gltf).perspectiveCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.first()
        assertEquals("Perspective", camera.name)
        assertEquals(Mat4.identity(), Mat4.of(*camera.transformation.matrix))
    }

    private fun assertEquals(expected: Mat4, actual: Mat4) {
        val array = actual.toArray()
        expected.toArray().forEachIndexed { i, value ->
            assertEquals(value, array[i], 0.001f)
        }
    }

    private fun String.asFile(): File {
        return File(CameraParserTest::class.java.getResource(this).toURI())
    }
}
