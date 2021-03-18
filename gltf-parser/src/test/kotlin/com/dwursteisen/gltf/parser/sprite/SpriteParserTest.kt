package com.dwursteisen.gltf.parser.sprite

import com.dwursteisen.gltf.parser.support.aseprite
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SpriteParserTest {

    @Test
    fun `parse - it parse aseprite file`() {
        val (resource, path) = aseprite("/sprite/animatedSprite.json")
        val parser = SpriteParser(path, resource)
        val result = parser.parse()
        val animations = result.sprites.values.first().animations
        assertEquals(2, animations.size)
    }
}
