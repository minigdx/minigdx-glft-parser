package com.dwursteisen.gltf.parser.sprite.internal

data class MetaData(
    val app: String,
    val version: String,
    val image: String,
    val format: String,
    val size: Size,
    val scale: String,
    val frameTags: List<FrameTag>,
    val slices: List<SliceData>
)
