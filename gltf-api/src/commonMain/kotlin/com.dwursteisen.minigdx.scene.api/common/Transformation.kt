package com.dwursteisen.minigdx.scene.api.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
class Transformation(
    @ProtoNumber(1)
    val matrix: FloatArray
)
