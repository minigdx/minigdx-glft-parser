package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class Joint(
    @ProtoNumber(0)
    val name: String,
    @ProtoNumber(1)
    val inverseGlobalTransformation: Transformation
)

@Serializable
class Armature(
    @ProtoNumber(0)
    val id: Int,
    @ProtoNumber(1)
    val name: String,
    @ProtoNumber(2)
    val joints: Array<Joint>
)
