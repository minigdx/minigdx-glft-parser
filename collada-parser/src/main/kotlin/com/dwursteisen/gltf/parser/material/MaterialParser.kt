package com.dwursteisen.gltf.parser.material

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.support.isEmissiveTexture
import com.dwursteisen.minigdx.scene.api.material.Material
import de.matthiasmann.twl.utils.PNGDecoder
import java.io.ByteArrayInputStream

class MaterialParser(private val gltfAsset: GltfAsset) {

    fun materials(): Map<String, Material> {
        return gltfAsset.materials
            // keep only materials using texture
            .filter { m -> m.isEmissiveTexture() }
            .map { m ->
                val buffer = m.emissiveTexture?.texture?.source?.bufferView!!
                val data = buffer.buffer.data.copyOfRange(buffer.byteOffset, buffer.byteOffset + buffer.byteLength)

                val decoder = PNGDecoder(ByteArrayInputStream(data))
                Material(
                    name = m.name ?: "",
                    id = m.index,
                    data = data,
                    width = decoder.width,
                    height = decoder.height
                )
            }.map {
                it.name to it
            }.toMap()
    }
}
