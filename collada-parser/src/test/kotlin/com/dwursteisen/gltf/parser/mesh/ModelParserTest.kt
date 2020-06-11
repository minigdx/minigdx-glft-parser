package com.dwursteisen.gltf.parser.mesh

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.assertPositionEquals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.model.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelParserTest {

    private val cube by gltf("/mesh/cube_translated.gltf")
    private val plane by gltf("/mesh/plane.gltf")

    @Test
    fun `parse | it parses a translated cube`() {
        val objects = ModelParser(cube).objects()

        assertEquals(1, objects.size)

        val cube = objects.getValue("Cube")
        val transformation = translation(Float3(1f, 2f, -3f))

        assertMat4Equals(transformation, Mat4.fromColumnMajor(*cube.transformation.matrix))
    }

    @Test
    fun `parse | it parses primitives of a cube`() {
        val objects = ModelParser(cube).objects()

        val cube = objects.getValue("Cube").mesh
        assertEquals(1, cube.primitives.size)
        // TODO: should try to be close to 8 instead
        assertEquals(24, cube.primitives.first().vertices.size)
    }

    @Test
    fun `parse | it parses a plane with correct coordinates`() {
        val objects = ModelParser(plane).objects()

        val cube = objects.getValue("Plane").mesh
        assertEquals(1, cube.primitives.size)
        assertEquals(4, cube.primitives.first().vertices.size)

        val (a, b, c, d) = cube.primitives.first().vertices

        assertPositionEquals(Position(0f, 0f, 0f), a.position)
        assertPositionEquals(Position(1f, 0f, 0f), b.position)
        assertPositionEquals(Position(0f, 0f, -2f), c.position)
        assertPositionEquals(Position(0f, 3f, 0f), d.position)
    }
}
