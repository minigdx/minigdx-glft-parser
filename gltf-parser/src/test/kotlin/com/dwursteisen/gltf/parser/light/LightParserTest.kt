package com.dwursteisen.gltf.parser.light

import com.dwursteisen.gltf.parser.ligts.LightParser
import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LightParserTest {

    private val lights by gltf("/lights/lights.gltf")

    @Test
    fun `pointLights | it returns point lights`() {
        val lights = LightParser(lights).pointLights()
        assertEquals(2, lights.values.size)

        val (_, point) = lights.values.toList()
        assertEquals(1f, point.position.x)
        assertEquals(3f, point.position.y)
        assertEquals(-2f, point.position.z)
    }
}
