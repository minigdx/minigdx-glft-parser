package com.dwursteisen.minigdx.scene.api.armature

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Joint(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val inverseGlobalTransformation: Transformation
)

@Serializable
class Armature(
    @ProtoId(0)
    val id: Id,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val joints: Array<Joint>
)
