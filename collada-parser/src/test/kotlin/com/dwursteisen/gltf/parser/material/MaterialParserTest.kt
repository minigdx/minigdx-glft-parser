package com.dwursteisen.gltf.parser.material

import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaterialParserTest {

    private val uv by gltf("/uv/multiple_materials.gltf")

    private val cube by gltf("/mesh/cube_translated.gltf")

    @Test
    fun `parse | it returns materials`() {
        val result = MaterialParser(uv).materials()
        assertEquals(2, result.size)
        assertEquals(4, result.values.first().width)
        assertEquals(1, result.values.first().height)
    }

    @Test
    fun `parse | it returns no material`() {
        val result = MaterialParser(cube).materials()
        assertEquals(0, result.size)
    }
}
