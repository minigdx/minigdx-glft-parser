package com.dwursteisen.minigdx.scene.api.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Transformation(
    @ProtoId(0)
    val matrix: FloatArray
)
