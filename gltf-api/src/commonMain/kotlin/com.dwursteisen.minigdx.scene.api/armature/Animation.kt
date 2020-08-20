package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class Frame(
    @ProtoNumber(0)
    val time: Float,
    @ProtoNumber(1)
    val globalTransformations: Array<Transformation>
)

@Serializable
class Animation(
    @ProtoNumber(0)
    val id: Int,
    @ProtoNumber(1)
    val armatureId: Int,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val duration: Float,
    @ProtoNumber(4)
    val frames: List<Frame> = emptyList()
)
