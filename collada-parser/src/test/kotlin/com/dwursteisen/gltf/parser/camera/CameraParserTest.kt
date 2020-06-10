package com.dwursteisen.gltf.parser.camera

import collada.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfCamera
import com.adrienben.tools.gltf.models.GltfCameraType
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.rotation
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.gltf.parser.support.transformation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
                val cam = node.children!!.first { it.camera != null }
                // Default camera orientation in blender is rotated by 90 on x and y.
                val transformation = node.transformation *
                        rotation(Float3(1f, 0f, 0f), -90f) *
                        rotation(Float3(0f, 1f, 0f), -90f)
                factory(node.name ?: "", cam.camera!!, transformation)
            }
    }

    fun orthographicCameras(): Map<String, OrthographicCamera> {
        val factory = { name: String, camera: GltfCamera, transformation: Mat4 ->
            OrthographicCamera(
                name = name,
                far = camera.orthographic?.zFar ?: 0f,
                near = camera.orthographic?.zNear ?: 0f,
                scale = camera.orthographic?.xMag ?: 0f,
                transformation = Transformation(transformation.asGLArray().toFloatArray())
            )
        }
        return source.convertToCameras(
            GltfCameraType.ORTHOGRAPHIC,
            factory
        ).map { it.name to it }
            .toMap()
    }

    fun perspectiveCameras(): Map<String, PerspectiveCamera> {
        val factory = { name: String, camera: GltfCamera, transformation: Mat4 ->
            PerspectiveCamera(
                name = name,
                far = camera.perspective?.zFar ?: 0f,
                near = camera.perspective?.zNear ?: 0f,
                fov = camera.perspective?.yFov ?: 90f,
                transformation = Transformation(transformation.asGLArray().toFloatArray())
            )
        }
        return source.convertToCameras(
            GltfCameraType.PERSPECTIVE,
            factory
        ).map { it.name to it }
            .toMap()
    }
}

class CameraParserTest {

    private val gltf by gltf("/camera/camera_default.gltf")

    @Test
    fun `parse | it parses orthographic camera`() {
        val cameras = CameraParser(gltf).orthographicCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Orthographic", camera.name)
        assertMat4Equals(Mat4.identity(), Mat4.fromColumnMajor(*camera.transformation.matrix))
    }

    @Test
    fun `parse | it parses perspective camera`() {
        val cameras = CameraParser(gltf).perspectiveCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Perspective", camera.name)
        assertMat4Equals(Mat4.identity(), Mat4.fromColumnMajor(*camera.transformation.matrix))
    }
}
