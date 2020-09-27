package com.dwursteisen.minigdx.scene.api.sprite

import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.model.UV
import kotlinx.serialization.Serializable

typealias AnimationName = String

@Serializable
data class Frame(
    val uvIndex: Int,
    val duration: Float
)

@Serializable
data class SpriteAnimation(
    val name: String,
    val duration: Float,
    val frames: List<Frame>
)

@Serializable
data class Sprite(
    val id: Id,
    val materialReference: Id,
    val animations: Map<AnimationName, SpriteAnimation>,
    val uvs: List<UV>
)
