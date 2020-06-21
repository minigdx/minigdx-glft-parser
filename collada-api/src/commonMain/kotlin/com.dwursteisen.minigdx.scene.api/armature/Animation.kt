package com.dwursteisen.minigdx.scene.api.armature

import collada.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Frame(
    @ProtoId(0)
    val time: Float,
    @ProtoId(1)
    val jointId: Int,
    @ProtoId(2)
    val localTransformation: Transformation
)

@Serializable
class Animation(
    @ProtoId(0)
    val id: Int,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val duration: Float,
    @ProtoId(3)
    val frames: List<Frame> = emptyList()
)
