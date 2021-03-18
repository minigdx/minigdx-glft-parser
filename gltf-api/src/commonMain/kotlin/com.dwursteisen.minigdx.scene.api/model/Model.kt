package com.dwursteisen.minigdx.scene.api.model

import com.dwursteisen.minigdx.scene.api.common.Id
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
data class Position(
    @ProtoNumber(1)
    var x: Float,
    @ProtoNumber(2)
    var y: Float,
    @ProtoNumber(3)
    var z: Float
)

@ExperimentalSerializationApi
@Serializable
data class Normal(
    @ProtoNumber(1)
    var x: Float,
    @ProtoNumber(2)
    var y: Float,
    @ProtoNumber(3)
    var z: Float
)

@ExperimentalSerializationApi
@Serializable
data class Color(
    @ProtoNumber(1)
    var r: Float,
    @ProtoNumber(2)
    var g: Float,
    @ProtoNumber(3)
    var b: Float,
    @ProtoNumber(4)
    var alpha: Float = 1.0f
) {

    companion object {
        val INVALID = Color(-1f, -1f, -1f)
    }
}

@ExperimentalSerializationApi
@Serializable
data class UV(
    @ProtoNumber(1)
    var x: Float,
    @ProtoNumber(2)
    var y: Float
) {

    companion object {
        val INVALID = UV(-1f, -1f)
    }
}

@ExperimentalSerializationApi
@Serializable
data class Influence(
    @ProtoNumber(2)
    var jointId: Int,
    @ProtoNumber(3)
    var weight: Float
)

@ExperimentalSerializationApi
@Serializable
data class Vertex(
    @ProtoNumber(1)
    var position: Position,
    @ProtoNumber(2)
    var normal: Normal,
    @ProtoNumber(3)
    var color: Color = Color.INVALID,
    @ProtoNumber(4)
    var uv: UV = UV.INVALID,
    @ProtoNumber(5)
    var influences: List<Influence> = emptyList()
)

@ExperimentalSerializationApi
@Serializable
data class Primitive(
    @ProtoNumber(1)
    val id: Id = Id(),
    @ProtoNumber(2)
    var vertices: List<Vertex> = emptyList(),
    @ProtoNumber(3)
    var verticesOrder: IntArray = intArrayOf(),
    @ProtoNumber(4)
    var materialId: Id = Id.None
)

@ExperimentalSerializationApi
@Serializable
data class Box(
    @ProtoNumber(1)
    val id: Id,
    @ProtoNumber(2)
    val name: String
)

@ExperimentalSerializationApi
@Serializable
data class Mesh(
    @ProtoNumber(1)
    var primitives: List<Primitive>
)

@ExperimentalSerializationApi
@Serializable
data class Model(
    @ProtoNumber(1)
    val id: Id,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    var mesh: Mesh
)
