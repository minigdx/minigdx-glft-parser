package com.dwursteisen.minigdx.scene.api.armature

import collada.Transformation
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Joint(
    @ProtoId(0)
    val id: Int,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    var childs: List<Joint> = emptyList(),
    @ProtoId(3)
    val inverseGlobalTransformation: Transformation
)

@Serializable
class Armature(
    @ProtoId(0)
    val id: Int,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val rootJoint: Joint
)
