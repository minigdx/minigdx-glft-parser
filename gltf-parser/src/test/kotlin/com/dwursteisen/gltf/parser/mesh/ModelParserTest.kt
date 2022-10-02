package com.dwursteisen.gltf.parser.mesh

import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.assertPositionEquals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.model.Position
import com.dwursteisen.minigdx.scene.api.model.UV
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ModelParserTest {

    private val cube by gltf("/mesh/cube_translated.gltf")
    private val plane by gltf("/mesh/plane.gltf")
    private val simpleUv by gltf("/uv/uv.gltf")
    private val multipleUv by gltf("/uv/multiple_materials.gltf")
    private val cubeWithJoints by gltf("/joints/cube_joints.gltf")
    private val cubeWithBoxes by gltf("/empty/cube_with_empty.gltf")

    private val ids = Dictionary()

    @Test
    fun `parse - it parses a translated cube`() {
        val objects = ModelParser(cube, ids).objects()

        assertEquals(1, objects.size)
    }

    @Test
    fun `parse - it parses primitives of a cube`() {
        val objects = ModelParser(cube, ids).objects()

        val cube = objects.values.first { it.name == "Cube" }.mesh
        assertEquals(1, cube.primitives.size)
        // TODO: should try to be close to 8 instead
        assertEquals(24, cube.primitives.first().vertices.size)
    }

    @Test
    fun `parse - it parses a plane with correct coordinates`() {
        val objects = ModelParser(plane, ids).objects()

        val cube = objects.values.first { it.name == "Plane" }.mesh
        assertEquals(1, cube.primitives.size)
        assertEquals(4, cube.primitives.first().vertices.size)

        val (a, b, c, d) = cube.primitives.first().vertices
            // Order by distance from (0, 0, 0) to always have the same order
            .sortedBy {
                it.position.x * it.position.x + it.position.y * it.position.y + it.position.z * it.position.z
            }

        assertPositionEquals(Position(0f, 0f, 0f), a.position)
        assertPositionEquals(Position(1f, 0f, 0f), b.position)
        assertPositionEquals(Position(0f, 0f, -2f), c.position)
        assertPositionEquals(Position(0f, 3f, 0f), d.position)
    }

    @Test
    fun `parse - it parses a mesh with no material`() {
        val objects = ModelParser(cube, ids).objects()
        val uvs = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .map { it.uv }
            .toSet()

        assertTrue(uvs.contains(UV.INVALID))
        assertEquals(1, uvs.size)
        assertEquals(Id.None, objects.values.first { it.name == "Cube" }.mesh.primitives.first().materialId)
    }

    @Test
    fun `parse - it parses a mesh with one material`() {
        val objects = ModelParser(simpleUv, ids).objects()
        val uvs = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .map { it.uv }

        assertTrue(uvs.isNotEmpty())
        assertFalse(uvs.contains(UV.INVALID))
    }

    @Test
    fun `parse - it parses a mesh with more than one material`() {
        val objects = ModelParser(multipleUv, ids).objects()
        val materials = objects.flatMap { it.value.mesh.primitives }
            .map { it.materialId }

        assertEquals(2, materials.size)
    }

    @Test
    fun `parse - it parses a mesh with influence`() {
        val objects = ModelParser(cubeWithJoints, ids).objects()
        val influences = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .flatMap { it.influences }

        assertTrue(influences.isNotEmpty())
    }

    private val kong by gltf("/joints/kong.gltf")

    @Test
    fun `parse - it parses a mesh with more influences`() {
        val objects = ModelParser(kong, ids).objects()
        val influences = objects.flatMap { it.value.mesh.primitives }
            .flatMap { it.vertices }
            .flatMap { it.influences }

        assertEquals(3760, influences.size)
    }
}
