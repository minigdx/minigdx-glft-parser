package com.dwursteisen.minigdx.scene.api.camera

import com.dwursteisen.minigdx.scene.api.common.Id
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

interface Camera {
    @ExperimentalSerializationApi
    val id: Id
    val name: String
}

@ExperimentalSerializationApi
@Serializable
data class PerspectiveCamera(
    @ProtoNumber(1)
    override val id: Id,
    @ProtoNumber(2)
    override val name: String,
    @ProtoNumber(3)
    val far: Float,
    @ProtoNumber(4)
    val near: Float,
    // in degrees
    @ProtoNumber(5)
    val fov: Float
) : Camera

@ExperimentalSerializationApi
@Serializable
data class OrthographicCamera(
    @ProtoNumber(1)
    override val id: Id,
    @ProtoNumber(2)
    override val name: String,
    @ProtoNumber(3)
    val far: Float,
    @ProtoNumber(4)
    val near: Float,
    @ProtoNumber(5)
    val scale: Float
) : Camera
