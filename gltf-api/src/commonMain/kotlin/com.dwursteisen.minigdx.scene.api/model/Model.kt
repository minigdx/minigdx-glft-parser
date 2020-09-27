package com.dwursteisen.minigdx.scene.api.model

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Position(
    @ProtoId(0)
    var x: Float,
    @ProtoId(1)
    var y: Float,
    @ProtoId(2)
    var z: Float
)

@Serializable
data class Normal(
    @ProtoId(0)
    var x: Float,
    @ProtoId(1)
    var y: Float,
    @ProtoId(2)
    var z: Float
)

@Serializable
data class Color(
    @ProtoId(0)
    var r: Float,
    @ProtoId(1)
    var g: Float,
    @ProtoId(2)
    var b: Float,
    @ProtoId(3)
    var alpha: Float = 1.0f
) {

    companion object {
        val INVALID = Color(-1f, -1f, -1f)
    }
}

@Serializable
data class UV(
    @ProtoId(0)
    var x: Float,
    @ProtoId(1)
    var y: Float
) {

    companion object {
        val INVALID = UV(-1f, -1f)
    }
}


@Serializable
data class Influence(
    @ProtoId(1)
    var jointId: Int,
    @ProtoId(2)
    var weight: Float
)

@Serializable
data class Vertex(
    @ProtoId(0)
    var position: Position,
    @ProtoId(1)
    var normal: Normal,
    @ProtoId(2)
    var color: Color = Color.INVALID,
    @ProtoId(3)
    var uv: UV = UV.INVALID,
    @ProtoId(4)
    var influences: List<Influence> = emptyList()
)

@Serializable
data class Primitive(
    @ProtoId(0)
    val id: Id = Id(),
    @ProtoId(1)
    var vertices: List<Vertex> = emptyList(),
    @ProtoId(2)
    var verticesOrder: IntArray = intArrayOf(),
    @ProtoId(3)
    var materialId: Id = Id.None
)

@Serializable
data class Box(
    @ProtoId(0)
    val id: Id,
    @ProtoId(1)
    val name: String
)

@Serializable
data class Mesh(
    @ProtoId(0)
    var primitives: List<Primitive>
)

@Serializable
data class Model(
    @ProtoId(0)
    val id: Id,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    var mesh: Mesh
)
