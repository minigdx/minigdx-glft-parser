package com.dwursteisen.gltf.parser.light

import com.dwursteisen.gltf.parser.lights.LightParser
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LightParserTest {

    private val lights by gltf("/lights/lights.gltf")

    private val ids = Dictionary()

    @Test
    fun `pointLights - it returns point lights`() {
        val lights = LightParser(lights, ids).pointLights()
        assertEquals(2, lights.values.size)
        val pointLight = lights.values.first()

        val color = pointLight.color
        assertEquals(color.r, 1.0f)
        assertEquals(color.g, 1.0f)
        assertEquals(color.b, 1.0f)
        assertEquals(color.alpha, 1.0f)

        assertEquals(pointLight.intensity, 1000)
    }
}
