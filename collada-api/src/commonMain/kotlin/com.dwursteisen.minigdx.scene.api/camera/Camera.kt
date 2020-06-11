package com.dwursteisen.minigdx.scene.api.camera

import collada.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

interface Camera {
    val name: String
    val transformation: Transformation
}

@Serializable
data class PerspectiveCamera(
    @ProtoId(0)
    override val name: String,
    @ProtoId(1)
    val far: Float,
    @ProtoId(2)
    val near: Float,
    @ProtoId(3)
    val fov: Float,
    @ProtoId(4)
    override val transformation: Transformation
) : Camera

@Serializable
data class OrthographicCamera(
    @ProtoId(0)
    override val name: String,
    @ProtoId(1)
    val far: Float,
    @ProtoId(2)
    val near: Float,
    @ProtoId(3)
    val scale: Float,
    @ProtoId(4)
    override val transformation: Transformation
) : Camera
