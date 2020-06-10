package com.dwursteisen.gltf.parser.mesh

import collada.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfMesh
import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.gltf.parser.support.toFloatArray
import com.dwursteisen.gltf.parser.support.transformation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

data class Position(val x: Float, val y: Float, val z: Float)
data class Normal(val x: Float, val y: Float, val z: Float)

data class Vertex(
    val position: Position,
    val normal: Normal
)

data class Primitive(
    val vertices: List<Vertex> = emptyList()
)

data class Mesh(
    val primitives: List<Primitive>
)

data class Object(
    val name: String,
    val transformation: Transformation,
    val mesh: Mesh
)

class ObjectParser(val gltfAsset: GltfAsset) {

    fun objects(): Map<String, Object> {
        val nodes = gltfAsset.nodes.filter { it.mesh != null }
        return nodes.map { it.toObject() }
            .map { it.name to it }
            .toMap()
    }

    private fun GltfNode.toObject(): Object {
        return Object(
            name = name!!,
            transformation = Transformation(transformation.asGLArray().toFloatArray()),
            mesh = this.mesh!!.toMesh()
        )
    }

    private fun GltfMesh.toMesh(): Mesh {
        val primitives = primitives.map { primitive ->
            val positions = primitive.attributes["POSITION"].toFloatArray()
                .toList()
                .chunked(3)
                .map { Position(it[0], it[1], it[2]) }

            val normals = primitive.attributes["NORMAL"].toFloatArray()
                .toList()
                .chunked(3)
                .map { Normal(it[0], it[1], it[2]) }

            val vertices = positions.zip(normals) { p, n ->
                Vertex(p, n)
            }

            Primitive(vertices)
        }
        return Mesh(primitives)
    }
}

class ObjectParserTest {

    private val cube by gltf("/mesh/cube_translated.gltf")

    @Test
    fun `parse | it parses a translated cube`() {
        val objects = ObjectParser(cube).objects()

        assertEquals(1, objects.size)

        val cube = objects.getValue("Cube")
        val transformation = translation(Float3(1f, 2f, -3f))

        assertMat4Equals(transformation, Mat4.fromColumnMajor(*cube.transformation.matrix))
    }

    @Test
    fun `parse | it parses primitives of a cube`() {
        val objects = ObjectParser(cube).objects()

        val cube = objects.getValue("Cube").mesh
        assertEquals(1, cube.primitives.size)
        // TODO: should try to be close to 8 instead
        assertEquals(24, cube.primitives.first().vertices.size)
    }
}
