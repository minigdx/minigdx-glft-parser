package com.dwursteisen.gltf.parser.material

import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.gltfResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class MaterialParserTest {

    private val uv = gltfResource("/uv/multiple_materials.gltf")

    private val cube = gltfResource("/mesh/cube_translated.gltf")

    private val alpha = gltfResource("/uv/alpha.gltf")

    private val bsdfTexture = gltfResource("/uv/bsdf_texture.gltf")

    private val externalTexture = gltfResource("/uv/bsdf_external_texture.gltf")

    private val ids = Dictionary()

    @Test
    fun `parse - it returns materials`() {
        val result = MaterialParser(uv.path, uv.asset, ids).materials()
        assertEquals(2, result.size)
        assertEquals(4, result.values.first().width)
        assertEquals(1, result.values.first().height)
        assertEquals(false, result.values.first().hasAlpha)
    }

    @Test
    fun `parse - it returns no material`() {
        val result = MaterialParser(cube.path, cube.asset, ids).materials()
        assertEquals(0, result.size)
    }

    @Test
    fun `parse - it parses material with alpha`() {
        val result = MaterialParser(alpha.path, alpha.asset, ids).materials()
        assertEquals(1, result.size)
        assertEquals(true, result.values.first().hasAlpha)
    }

    @Test
    fun `parse - it parses bsdf texture`() {
        val result = MaterialParser(bsdfTexture.path, bsdfTexture.asset, ids).materials()
        assertEquals(1, result.size)
        assertEquals(false, result.values.first().hasAlpha)
    }

    @Test
    fun `parse - it parses external texture`() {
        val result = MaterialParser(externalTexture.path, externalTexture.asset, ids).materials()
        assertEquals(1, result.size)
        // no alpha on the texture
        val material = result.values.first()
        assertFalse(material.hasAlpha)
        // the texture is external
        assertTrue(material.isExternal)
        assertEquals(0, material.data.size)
        assertEquals(material.uri, "textures/Untitled.png")
    }
}
