package com.dwursteisen.minigdx.scene.api.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@ExperimentalSerializationApi
@Serializable
class Transformation(
    @ProtoNumber(1)
    val translation: FloatArray,
    @ProtoNumber(2)
    val rotation: FloatArray,
    @ProtoNumber(3)
    val scale: FloatArray
)
