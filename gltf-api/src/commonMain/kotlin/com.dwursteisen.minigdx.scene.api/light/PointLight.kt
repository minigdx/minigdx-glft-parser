package com.dwursteisen.minigdx.scene.api.light

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.model.Color
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId


interface Light {
    val id: Id
    val name: String
}

@Serializable
data class PointLight(
    @ProtoId(0)
    override val id: Id,
    @ProtoId(1)
    override val name: String,
    @ProtoId(2)
    val color: Color,
    @ProtoId(3)
    val intensity: Int
) : Light
