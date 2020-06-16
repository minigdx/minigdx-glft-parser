package com.dwursteisen.gltf.parser.material

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.support.isEmissiveTexture
import com.dwursteisen.minigdx.scene.api.material.Material
import de.matthiasmann.twl.utils.PNGDecoder
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

class MaterialParser(private val gltfAsset: GltfAsset) {

    fun materials(): Map<String, Material> {
        return gltfAsset.materials
            // keep only materials using texture
            .filter { m -> m.isEmissiveTexture() }
            .map { m ->
                val buffer = m.emissiveTexture?.texture?.source?.bufferView!!
                val data = buffer.buffer.data.copyOfRange(buffer.byteOffset, buffer.byteOffset + buffer.byteLength)

                val decoder = PNGDecoder(ByteArrayInputStream(data))
                // create a byte buffer big enough to store RGBA values
                val txt =
                    ByteBuffer.allocateDirect(4 * decoder.width * decoder.height)

                // decode
                decoder.decode(txt, decoder.width * 4, PNGDecoder.Format.RGBA)

                // flip the buffer so its ready to read
                txt.flip()

                val result = ByteArray(txt.remaining())
                txt.get(result)

                Material(
                    name = m.name ?: "",
                    id = m.index,
                    data = result,
                    width = decoder.width,
                    height = decoder.height
                )
            }.map {
                it.name to it
            }.toMap()
    }
}
