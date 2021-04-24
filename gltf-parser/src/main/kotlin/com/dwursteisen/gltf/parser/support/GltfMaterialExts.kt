package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfImage
import com.adrienben.tools.gltf.models.GltfMaterial

fun GltfMaterial?.isEmissiveTexture(): Boolean {
    return this != null && this.emissiveTexture?.texture?.source != null
}

fun GltfMaterial?.isBSDFTexture(): Boolean {
    return this != null && this.pbrMetallicRoughness.baseColorTexture?.texture?.source != null
}

fun GltfMaterial?.isSupportedTexture(): Boolean {
    return this.isEmissiveTexture() || this.isBSDFTexture()
}

val GltfMaterial?.source: GltfImage?
    get() {
        if (this == null) return null
        return if (isEmissiveTexture()) {
            this.emissiveTexture?.texture?.source
        } else if (isBSDFTexture()) {
            this.pbrMetallicRoughness.baseColorTexture?.texture?.source
        } else {
            null
        }
    }
