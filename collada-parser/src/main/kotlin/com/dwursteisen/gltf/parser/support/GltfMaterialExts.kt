package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfMaterial

fun GltfMaterial?.isEmissiveTexture(): Boolean {
    return this != null && this.emissiveTexture?.texture?.source != null
}
