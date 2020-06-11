package com.dwursteisen.gltf.parser.camera

import collada.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfCamera
import com.adrienben.tools.gltf.models.GltfCameraType
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.rotation
import com.dwursteisen.gltf.parser.support.transformation
import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera

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
                        rotation(
                            Float3(
                                1f,
                                0f,
                                0f
                            ), -90f
                        ) *
                        rotation(
                            Float3(
                                0f,
                                1f,
                                0f
                            ), -90f
                        )
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
