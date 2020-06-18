package com.dwursteisen.minigdx.scene.api.light

import com.dwursteisen.minigdx.scene.api.model.Color
import com.dwursteisen.minigdx.scene.api.model.Position
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
data class PointLight(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val position: Position,
    @ProtoId(2)
    val color: Color,
    @ProtoId(3)
    val intensity: Int
)
