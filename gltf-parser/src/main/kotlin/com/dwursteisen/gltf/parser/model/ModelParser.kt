package com.dwursteisen.gltf.parser.model

import com.dwursteisen.minigdx.scene.api.common.Transformation
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfMesh
import com.adrienben.tools.gltf.models.GltfNode
import com.dwursteisen.gltf.parser.support.*
import com.dwursteisen.minigdx.scene.api.model.*

class ModelParser(private val gltfAsset: GltfAsset) {

    fun objects(): Map<String, Model> {
        val nodes = gltfAsset.nodes.filter { it.mesh != null }
        return nodes.mapIndexed { index, it -> it.toObject().copy(id = index) }
            .map { it.name to it }
            .toMap()
    }

    fun boxes(): Map<String, Boxe> {
        return gltfAsset.nodes.filter { it.isBox }
            .mapIndexed { index, it ->
                Boxe(
                    id = index,
                    name = it.name ?: "",
                    transformation = Transformation(it.transformation.asGLArray().toFloatArray())
                )
            }.map { it.name to it }
            .toMap()
    }

    private fun GltfNode.toObject(): Model {
        val armatureId = gltfAsset.skin?.indexOf(skin) ?: -1
        return Model(
            name = name!!,
            transformation = Transformation(
                transformation.asGLArray().toFloatArray()
            ),
            mesh = this.mesh!!.toMesh(),
            armatureId = armatureId,
            boxes = this.children.toBoxes()
        )
    }

    private fun List<GltfNode>?.toBoxes(): List<Boxe> {
        val boxes = this?.filter { it.isBox } ?: emptyList()

        return boxes.map {
            Boxe(
                name = it.name ?: "",
                transformation = Transformation(it.transformation.asGLArray().toFloatArray())
            )
        }
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

            val colors = primitive.attributes["COLOR_0"].toFloatArray()
                .toList()
                .chunked(4)
                .map { Color(it[0], it[1], it[2], it[3]) }

            val joints = primitive.attributes["JOINTS_0"].toIntArray()
                .toList()
                .chunked(4)

            val weights = primitive.attributes["WEIGHTS_0"].toFloatArray()
                .toList()
                .chunked(4)

            val influences = joints.zip(weights) { j, w ->
                val (j1, j2, j3, j4) = j
                val (w1, w2, w3, w4) = w
                listOf(
                    Influence(j1, w1),
                    Influence(j2, w2),
                    Influence(j3, w3),
                    Influence(j4, w4)
                )
            }

            val material = gltfAsset.materials.getOrNull(primitive.material.index)
            val uvs = if (!material.isEmissiveTexture()) {
                emptyList()
            } else {
                primitive.attributes["TEXCOORD_0"].toFloatArray()
                    .toList()
                    .chunked(2)
                    .map { UV(it[0], it[1]) }
            }
            val vertices = positions.mapIndexed { index, p ->
                val n = normals[index]
                val c = colors.getOrElse(index) { Color.INVALID }
                val uv = uvs.getOrElse(index) { UV.INVALID }
                val influence = influences.getOrElse(index) { emptyList() }
                Vertex(
                    position = p,
                    normal = n,
                    color = c,
                    uv = uv,
                    influences = influence
                )
            }

            val materialId = if (primitive.material.isEmissiveTexture()) {
                primitive.material.index
            } else {
                -1
            }
            Primitive(
                vertices = vertices,
                verticesOrder = primitive.indices.toIntArray(),
                materialId = materialId
            )
        }
        return Mesh(
            primitives = primitives
        )
    }
}
