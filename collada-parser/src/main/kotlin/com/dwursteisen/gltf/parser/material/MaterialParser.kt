package com.dwursteisen.gltf.parser.material

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.support.isEmissiveTexture
import com.dwursteisen.minigdx.scene.api.model.Material

class MaterialParser(private val gltfAsset: GltfAsset) {

    fun materials(): Map<String, Material> {
        return gltfAsset.materials
            // keep only materials using texture
            .filter { m -> m.isEmissiveTexture() }
            .map { m ->
                val buffer = m.emissiveTexture?.texture?.source?.bufferView!!
                val data = buffer.buffer.data.copyOfRange(buffer.byteOffset, buffer.byteOffset + buffer.byteLength)
                Material(
                    name = m.name ?: "",
                    id = m.index,
                    data = data
                )
            }.map {
                it.name to it
            }.toMap()
    }
}
