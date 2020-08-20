package com.dwursteisen.minigdx.scene.api.camera

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

interface Camera {
    val name: String
    val transformation: Transformation
}

@Serializable
data class PerspectiveCamera(
    @ProtoNumber(0)
    override val name: String,
    @ProtoNumber(1)
    val far: Float,
    @ProtoNumber(2)
    val near: Float,
    @ProtoNumber(3)
    val fov: Float,
    @ProtoNumber(4)
    override val transformation: Transformation
) : Camera

@Serializable
data class OrthographicCamera(
    @ProtoNumber(0)
    override val name: String,
    @ProtoNumber(1)
    val far: Float,
    @ProtoNumber(2)
    val near: Float,
    @ProtoNumber(3)
    val scale: Float,
    @ProtoNumber(4)
    override val transformation: Transformation
) : Camera
