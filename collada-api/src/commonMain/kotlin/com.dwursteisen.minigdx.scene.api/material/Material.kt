package com.dwursteisen.minigdx.scene.api.material

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Material(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val id: Int,
    @ProtoId(2)
    val width: Int,
    @ProtoId(3)
    val height: Int,
    @ProtoId(4)
    val data: ByteArray
)
