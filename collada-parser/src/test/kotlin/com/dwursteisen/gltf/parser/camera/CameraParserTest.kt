package com.dwursteisen.gltf.parser.camera

import com.curiouscreature.kotlin.math.Mat4
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
