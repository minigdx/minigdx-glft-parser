package com.dwursteisen.minigdx.scene.api.material

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class Material(
    @ProtoNumber(0)
    val name: String,
    @ProtoNumber(1)
    val id: Int,
    @ProtoNumber(2)
    val width: Int,
    @ProtoNumber(3)
    val height: Int,
    @ProtoNumber(4)
    val data: ByteArray
)
