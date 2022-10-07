package com.dwursteisen.minigdx.scene.api.material

import com.dwursteisen.minigdx.scene.api.common.Id
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class Material(
    @ProtoNumber(1)
    val id: Id,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val width: Int,
    @ProtoNumber(4)
    val height: Int,
    @ProtoNumber(5)
    val data: ByteArray,
    @ProtoNumber(6)
    val hasAlpha: Boolean = false,
    @ProtoNumber(7)
    val uri: String?,
    @ProtoNumber(8)
    val isExternal: Boolean,
)
