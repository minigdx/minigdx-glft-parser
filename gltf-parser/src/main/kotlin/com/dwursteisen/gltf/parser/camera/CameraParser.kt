package com.dwursteisen.gltf.parser.camera

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfCamera
import com.adrienben.tools.gltf.models.GltfCameraType
import com.curiouscreature.kotlin.math.degrees
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera
import com.dwursteisen.minigdx.scene.api.common.Id

class CameraParser(private val source: GltfAsset, private val ids: Dictionary) {

    private fun <T : Camera> GltfAsset.convertToCameras(
        type: GltfCameraType,
        factory: (name: String, camera: GltfCamera) -> T
    ): List<T> {
        // Only select nodes attached to a camera
        val cameras = this.nodes.filter { node ->
            node.camera != null && node.camera?.type == type
        }

        // Create cameras using those nodes
        return cameras
            .map { node ->
                val camera = node.camera!!
                factory(camera.name ?: "", camera)
            }
    }

    fun orthographicCameras(): Map<Id, OrthographicCamera> {
        val factory = { name: String, camera: GltfCamera ->
            OrthographicCamera(
                id = ids.get(camera),
                name = name,
                far = camera.orthographic?.zFar ?: 0f,
                near = camera.orthographic?.zNear ?: 0f,
                scale = camera.orthographic?.xMag ?: 0f
            )
        }
        return source.convertToCameras(
            GltfCameraType.ORTHOGRAPHIC,
            factory
        ).associateBy { it.id }
    }

    fun perspectiveCameras(): Map<Id, PerspectiveCamera> {
        val factory = { name: String, camera: GltfCamera ->
            PerspectiveCamera(
                id = ids.get(camera),
                name = name,
                far = camera.perspective?.zFar ?: 0f,
                near = camera.perspective?.zNear ?: 0f,
                fov = camera.perspective?.yFov?.let { degrees(it) } ?: 90f
            )
        }
        return source.convertToCameras(
            GltfCameraType.PERSPECTIVE,
            factory
        ).associateBy { it.id }
    }
}
