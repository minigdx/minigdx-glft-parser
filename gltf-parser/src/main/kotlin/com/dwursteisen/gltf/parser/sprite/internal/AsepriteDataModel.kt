package com.dwursteisen.gltf.parser.sprite.internal

data class AsepriteDataModel(
    val frames: Map<FrameName, FrameDetail>,
    val meta: MetaData
)
