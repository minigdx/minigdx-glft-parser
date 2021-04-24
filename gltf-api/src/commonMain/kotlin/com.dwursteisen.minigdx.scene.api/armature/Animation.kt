package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class Frame(
    @ProtoNumber(1)
    val time: Float,
    @ProtoNumber(2)
    val globalTransformations: Array<Transformation>
)

@ExperimentalSerializationApi
@Serializable
class Animation(
    @ProtoNumber(1)
    val id: Id,
    @ProtoNumber(2)
    val armatureId: Id,
    @ProtoNumber(3)
    val name: String,
    @ProtoNumber(4)
    val duration: Float,
    @ProtoNumber(5)
    val frames: List<Frame> = emptyList()
)
