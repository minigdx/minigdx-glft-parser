package com.dwursteisen.gltf.parser.sprite.internal

data class FrameDetail(
    val frame: Frame,
    val rotated: Boolean,
    val trimmed: Boolean,
    val duration: Millisecond
)
