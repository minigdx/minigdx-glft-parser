package com.dwursteisen.gltf.parser.mesh

import collada.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.translation
import com.curiouscreature.kotlin.math.transpose
import com.dwursteisen.gltf.parser.support.assertMat4Equals
import com.dwursteisen.gltf.parser.support.gltf
import com.dwursteisen.gltf.parser.support.transformation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

data class Mesh(val todo: Any)

data class Object(val name: String,
                  val transformation: Transformation,
                  val mesh: Mesh)

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
            transformation = Transformation(transformation.toFloatArray()),
            mesh = Mesh(Unit)
        )
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

        assertMat4Equals(transformation, Mat4.of(*cube.transformation.matrix))
    }
}
