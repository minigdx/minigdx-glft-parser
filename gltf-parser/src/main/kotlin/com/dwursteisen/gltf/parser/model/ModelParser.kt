package com.dwursteisen.gltf.parser.model

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfMesh
import com.adrienben.tools.gltf.models.GltfNode
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.isBox
import com.dwursteisen.gltf.parser.support.isSupportedTexture
import com.dwursteisen.gltf.parser.support.toChunckedFloat
import com.dwursteisen.gltf.parser.support.toChunckedInt
import com.dwursteisen.gltf.parser.support.toIntArray
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.model.Box
import com.dwursteisen.minigdx.scene.api.model.Color
import com.dwursteisen.minigdx.scene.api.model.Influence
import com.dwursteisen.minigdx.scene.api.model.Mesh
import com.dwursteisen.minigdx.scene.api.model.Model
import com.dwursteisen.minigdx.scene.api.model.Normal
import com.dwursteisen.minigdx.scene.api.model.Position
import com.dwursteisen.minigdx.scene.api.model.Primitive
import com.dwursteisen.minigdx.scene.api.model.UV
import com.dwursteisen.minigdx.scene.api.model.Vertex

class ModelParser(private val gltfAsset: GltfAsset, private val ids: Dictionary) {

    fun objects(): Map<Id, Model> {
        val nodes = gltfAsset.nodes.filter { it.mesh != null }
        return nodes.map { it.toObject() }.associateBy { it.id }
    }

    fun boxes(): Map<Id, Box> {
        return gltfAsset.nodes.filter { it.isBox }
            .map { it ->
                Box(
                    id = ids.get(it),
                    name = it.name ?: ""
                )
            }.associateBy { it.id }
    }

    private fun GltfNode.toObject(): Model {
        return Model(
            id = ids.get(this.mesh!!),
            name = name!!,
            mesh = this.mesh!!.toMesh()
        )
    }

    private fun GltfMesh.toMesh(): Mesh {
        val primitives = primitives.map { primitive ->
            val positions = primitive.attributes["POSITION"]
                .toChunckedFloat()
                .map { Position(it[0], it[1], it[2]) }

            val normals = primitive.attributes["NORMAL"]
                .toChunckedFloat()
                .map { Normal(it[0], it[1], it[2]) }

            val colors = primitive.attributes["COLOR_0"]
                .toChunckedFloat()
                .map { Color(it[0], it[1], it[2], it[3]) }

            val joints = primitive.attributes["JOINTS_0"]
                .toChunckedInt()

            val weights = primitive.attributes["WEIGHTS_0"]
                .toChunckedFloat()

            val influences: List<List<Influence>> = joints.zip(weights) { j, w ->
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
            val uvs = if (!material.isSupportedTexture()) {
                emptyList()
            } else {
                primitive.attributes["TEXCOORD_0"]
                    .toChunckedFloat()
                    .map { UV(it[0], it[1]) }
            }
            val vertices = positions.mapIndexed { index, p ->
                val n = normals[index]
                val c = colors.getOrElse(index) { Color.INVALID }
                val uv = uvs.getOrElse(index) { UV.INVALID }
                val influence = influences.getOrElse(index) {
                    emptyList()
                }
                Vertex(
                    position = p,
                    normal = n,
                    color = c,
                    uv = uv,
                    influences = influence
                )
            }

            val materialId = if (primitive.material.isSupportedTexture()) {
                ids.get(primitive.material)
            } else {
                Id.None
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
