package com.dwursteisen.minigdx.scene.api.light

import com.dwursteisen.minigdx.scene.api.model.Color
import com.dwursteisen.minigdx.scene.api.model.Position
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class PointLight(
    @ProtoNumber(0)
    val name: String,
    @ProtoNumber(1)
    val position: Position,
    @ProtoNumber(2)
    val color: Color,
    @ProtoNumber(3)
    val intensity: Int
)
