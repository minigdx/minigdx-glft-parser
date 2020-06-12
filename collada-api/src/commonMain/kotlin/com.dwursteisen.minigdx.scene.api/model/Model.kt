package com.dwursteisen.minigdx.scene.api.model

import collada.Transformation
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
)

@Serializable
data class UV(
    @ProtoId(0)
    val x: Float,
    @ProtoId(1)
    val y: Float
)

@Serializable
data class Vertex(
    @ProtoId(0)
    val position: Position,
    @ProtoId(1)
    val normal: Normal,
    @ProtoId(2)
    val color: Color? = null,
    @ProtoId(3)
    val uv: UV? = null
)

@Serializable
data class Primitive(
    @ProtoId(0)
    val vertices: List<Vertex> = emptyList(),
    @ProtoId(1)
    val materialId: Int? = null
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
    val mesh: Mesh
)
