package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Frame(
    @ProtoId(0)
    val time: Float,
    @ProtoId(1)
    val globalTransformations: Array<Transformation>
)

@Serializable
class Animation(
    @ProtoId(0)
    val id: Int,
    @ProtoId(1)
    val armatureId: Int,
    @ProtoId(2)
    val name: String,
    @ProtoId(3)
    val duration: Float,
    @ProtoId(4)
    val frames: List<Frame> = emptyList()
)
