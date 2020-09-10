package com.dwursteisen.minigdx.scene.api.camera

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

interface Camera {
    val id: Id
    val name: String
}

@Serializable
data class PerspectiveCamera(
    @ProtoId(0)
    override val id: Id,
    @ProtoId(1)
    override val name: String,
    @ProtoId(2)
    val far: Float,
    @ProtoId(3)
    val near: Float,
    @ProtoId(4)
    val fov: Float
) : Camera

@Serializable
data class OrthographicCamera(
    @ProtoId(0)
    override val id: Id,
    @ProtoId(1)
    override val name: String,
    @ProtoId(2)
    val far: Float,
    @ProtoId(3)
    val near: Float,
    @ProtoId(4)
    val scale: Float
) : Camera
