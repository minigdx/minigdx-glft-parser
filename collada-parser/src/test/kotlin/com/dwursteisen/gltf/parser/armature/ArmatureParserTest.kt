package com.dwursteisen.gltf.parser.armature

import com.dwursteisen.gltf.parser.support.gltf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ArmatureParserTest {

    private val cube by gltf("/joints/cube_joints.gltf")
    private val cubeAnimated by gltf("/joints/cube_joints_animated.gltf")
    private val noJoint by gltf("/mesh/cube_translated.gltf")

    @Test
    fun `parse | it parse armatures`() {
        val parser = ArmatureParser(cube)
        assertEquals(1, parser.armatures().size)
        assertTrue(parser.animations().isEmpty())
    }

    @Test
    fun `parse | when there is no armature, it parse nothing`() {
        val parser = ArmatureParser(noJoint)
        assertTrue(parser.armatures().isEmpty())
        assertTrue(parser.animations().isEmpty())
    }

    @Test
    fun `parse | it parse animations`() {
        val parser = ArmatureParser(cubeAnimated)
        assertEquals(1, parser.armatures().size)
        assertEquals(1, parser.animations().size)
    }
}
