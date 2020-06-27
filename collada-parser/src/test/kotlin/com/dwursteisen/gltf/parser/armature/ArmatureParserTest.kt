package com.dwursteisen.gltf.parser.armature

import com.curiouscreature.kotlin.math.Mat4
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.Scene
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ArmatureParserTest {

    private val simpleAnimation by gltf("/joints/cube.gltf")
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
        val animations = parser.animations()
        assertEquals(1, parser.armatures().size)
        assertEquals(1, animations.size)

        val animation = animations.values.first()
        assertEquals(5, animation.first().frames.size)
    }

    @Test
    fun `animations | it returns computed animations`() {
        val parser = ArmatureParser(cubeAnimated)
        val scene = Scene(
            armatures = parser.armatures(),
            animations = parser.animations()
        )

        assertEquals(1, scene.animations.values.first().size)
    }

    @Test
    fun `animations | it returns computed correct animations`() {
        val parser = ArmatureParser(simpleAnimation)
        val scene = Scene(
            armatures = parser.armatures(),
            animations = parser.animations()
        )

        val keyframes = scene.animations.values.first().first()
        val (first, second, third) = keyframes.frames
        assertEquals(1f, Mat4.fromColumnMajor(*first.globalTransformations.first().matrix).translation.x)
        assertEquals(2f, Mat4.fromColumnMajor(*second.globalTransformations.first().matrix).translation.x)
        assertEquals(0f, Mat4.fromColumnMajor(*third.globalTransformations.first().matrix).translation.x)
    }
}
