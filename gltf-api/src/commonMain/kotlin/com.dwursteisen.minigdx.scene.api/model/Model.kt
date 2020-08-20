package com.dwursteisen.minigdx.scene.api.model

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class Position(
    @ProtoNumber(0)
    val x: Float,
    @ProtoNumber(1)
    val y: Float,
    @ProtoNumber(2)
    val z: Float
)

@Serializable
data class Normal(
    @ProtoNumber(0)
    val x: Float,
    @ProtoNumber(1)
    val y: Float,
    @ProtoNumber(2)
    val z: Float
)

@Serializable
data class Color(
    @ProtoNumber(0)
    val r: Float,
    @ProtoNumber(1)
    val g: Float,
    @ProtoNumber(2)
    val b: Float,
    @ProtoNumber(3)
    val alpha: Float = 1.0f
) {

    companion object {
        val INVALID = Color(-1f, -1f, -1f)
    }
}

@Serializable
data class UV(
    @ProtoNumber(0)
    val x: Float,
    @ProtoNumber(1)
    val y: Float
) {

    companion object {
        val INVALID = UV(-1f, -1f)
    }
}


@Serializable
class Influence(
    @ProtoNumber(1)
    val jointId: Int,
    @ProtoNumber(2)
    val weight: Float
)

@Serializable
data class Vertex(
    @ProtoNumber(0)
    val position: Position,
    @ProtoNumber(1)
    val normal: Normal,
    @ProtoNumber(2)
    val color: Color = Color.INVALID,
    @ProtoNumber(3)
    val uv: UV = UV.INVALID,
    @ProtoNumber(4)
    val influences: List<Influence> = emptyList()
)

@Serializable
class Primitive(
    @ProtoNumber(0)
    val vertices: List<Vertex> = emptyList(),
    @ProtoNumber(1)
    val verticesOrder: IntArray = intArrayOf(),
    @ProtoNumber(2)
    val materialId: Int = -1
)

@Serializable
data class Boxe(
    @ProtoNumber(0)
    val name: String,
    @ProtoNumber(1)
    val transformation: Transformation
)

@Serializable
data class Mesh(
    @ProtoNumber(0)
    val primitives: List<Primitive>
)

@Serializable
data class Model(
    @ProtoNumber(0)
    val name: String,
    @ProtoNumber(1)
    val transformation: Transformation,
    @ProtoNumber(2)
    val mesh: Mesh,
    @ProtoNumber(3)
    val armatureId: Int = -1,
    @ProtoNumber(4)
    val boxes: List<Boxe> = emptyList()
)
