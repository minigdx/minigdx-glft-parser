package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class Joint(
    @ProtoNumber(1)
    val name: String,
    @ProtoNumber(2)
    val inverseGlobalTransformation: Transformation
)

@ExperimentalSerializationApi
@Serializable
class Armature(
    @ProtoNumber(1)
    val id: Id,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val joints: Array<Joint>
)
