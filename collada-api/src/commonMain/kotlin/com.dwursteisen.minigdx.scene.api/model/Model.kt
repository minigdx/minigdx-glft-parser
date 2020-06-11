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
data class Vertex(
    @ProtoId(0)
    val position: Position,
    @ProtoId(1)
    val normal: Normal
)

@Serializable
data class Primitive(
    @ProtoId(0)
    val vertices: List<Vertex> = emptyList()
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
