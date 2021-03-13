package com.dwursteisen.gltf.parser.camera

import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CameraParserTest {

    private val gltf by gltf("/camera/camera_default.gltf")

    private val ids = Dictionary()

    @Test
    fun `parse - it parses orthographic camera`() {
        val cameras = CameraParser(gltf, ids).orthographicCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Orthographic", camera.name)
    }

    @Test
    fun `parse - it parses perspective camera`() {
        val cameras = CameraParser(gltf, ids).perspectiveCameras()
        assertEquals(1, cameras.size)

        val camera = cameras.values.first()
        assertEquals("Perspective", camera.name)

        assertEquals(100f, camera.far)
        assertEquals(0.1f, camera.near)
        assertEquals(22.895191f, camera.fov, 1f)
    }
}
