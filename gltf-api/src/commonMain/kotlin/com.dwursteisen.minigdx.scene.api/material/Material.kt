package com.dwursteisen.minigdx.scene.api.material

import com.dwursteisen.minigdx.scene.api.common.Id
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Material(
    @ProtoId(0)
    val id: Id,
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val width: Int,
    @ProtoId(3)
    val height: Int,
    @ProtoId(4)
    val data: ByteArray,
    @ProtoId(5)
    val hasAlpha: Boolean = false
)
