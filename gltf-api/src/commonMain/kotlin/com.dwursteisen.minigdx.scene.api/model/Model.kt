package com.dwursteisen.minigdx.scene.api.model

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Position(
    @ProtoId(0)
    val x: Float,
    @ProtoId(1)
    val y: Float,
    @ProtoId(2)
    val z: Float
)

@Serializable
data class Normal(
    @ProtoId(0)
    val x: Float,
    @ProtoId(1)
    val y: Float,
    @ProtoId(2)
    val z: Float
)

@Serializable
data class Color(
    @ProtoId(0)
    val r: Float,
    @ProtoId(1)
    val g: Float,
    @ProtoId(2)
    val b: Float,
    @ProtoId(3)
    val alpha: Float = 1.0f
) {

    companion object {
        val INVALID = Color(-1f, -1f, -1f)
    }
}

@Serializable
data class UV(
    @ProtoId(0)
    val x: Float,
    @ProtoId(1)
    val y: Float
) {

    companion object {
        val INVALID = UV(-1f, -1f)
    }
}


@Serializable
class Influence(
    @ProtoId(1)
    val jointId: Int,
    @ProtoId(2)
    val weight: Float
)

@Serializable
data class Vertex(
    @ProtoId(0)
    val position: Position,
    @ProtoId(1)
    val normal: Normal,
    @ProtoId(2)
    val color: Color = Color.INVALID,
    @ProtoId(3)
    val uv: UV = UV.INVALID,
    @ProtoId(4)
    val influences: List<Influence> = emptyList()
)

@Serializable
class Primitive(
    @ProtoId(0)
    val vertices: List<Vertex> = emptyList(),
    @ProtoId(1)
    val verticesOrder: IntArray = intArrayOf(),
    @ProtoId(2)
    val materialId: Int = -1
)

@Serializable
data class Boxe(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val transformation: Transformation
)

@Serializable
data class Mesh(
    @ProtoId(0)
    val primitives: List<Primitive>
)

@Serializable
data class Model(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val transformation: Transformation,
    @ProtoId(2)
    val mesh: Mesh,
    @ProtoId(3)
    val armatureId: Int = -1,
    @ProtoId(4)
    val boxes: List<Boxe> = emptyList()
)
