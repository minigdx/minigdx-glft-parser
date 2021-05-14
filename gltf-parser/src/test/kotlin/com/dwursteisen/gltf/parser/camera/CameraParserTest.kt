package com.dwursteisen.gltf.parser.camera

import com.dwursteisen.gltf.parser.scene.SceneParser
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.combined
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.relation.ObjectType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CameraParserTest {

    private val defaultCamera by gltf("/camera/camera_default.gltf")

    private val rotationCamera by gltf("/camera/camera_rotation.gltf")

    private val cameraChildren by gltf("/camera/camera_child.gltf")

    private val ids = Dictionary()

    @Test
    fun `parse - it parses orthographic camera`() {
        val cameras = CameraParser(defaultCamera, ids).orthographicCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Orthographic", camera.name)
    }

    @Test
    fun `parse - it parses perspective camera`() {
        val cameras = CameraParser(defaultCamera, ids).perspectiveCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Perspective", camera.name)

        assertEquals(100f, camera.far)
        assertEquals(0.1f, camera.near)
        assertEquals(22.895191f, camera.fov, 1f)
    }

    @Test
    fun `parse - it parses camera with children`() {
        val scene = SceneParser(cameraChildren).parse()
        val camera = scene.children.first { it.type == ObjectType.CAMERA }
        val child = camera.children.first().transformation.combined
        val childPosition = camera.transformation.combined * child
        assertEquals(0f, childPosition.translation.x, 0.01f)
        assertEquals(0f, childPosition.translation.y, 0.01f)
        assertEquals(0f, childPosition.translation.z, 0.01f)
    }
}
