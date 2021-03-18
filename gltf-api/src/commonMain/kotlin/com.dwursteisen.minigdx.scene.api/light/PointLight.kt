package com.dwursteisen.minigdx.scene.api.light

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.model.Color
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

interface Light {
    @ExperimentalSerializationApi
    val id: Id
    val name: String
}

@ExperimentalSerializationApi
@Serializable
data class PointLight(
    @ProtoNumber(1)
    override val id: Id,
    @ProtoNumber(2)
    override val name: String,
    @ProtoNumber(3)
    val color: Color,
    @ProtoNumber(4)
    val intensity: Int
) : Light
