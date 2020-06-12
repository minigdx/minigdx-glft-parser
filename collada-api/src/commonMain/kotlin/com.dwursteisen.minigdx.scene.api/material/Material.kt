package com.dwursteisen.minigdx.scene.api.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class Material(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val id: Int,
    @ProtoId(2)
    val data: ByteArray
)
