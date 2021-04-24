package com.dwursteisen.gltf.parser.material

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.isSupportedTexture
import com.dwursteisen.gltf.parser.support.source
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.material.Material
import de.matthiasmann.twl.utils.PNGDecoder
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

class MaterialParser(private val gltfAsset: GltfAsset, private val ids: Dictionary) {

    fun materials(): Map<Id, Material> {
        return gltfAsset.materials
            // keep only materials using texture
            .filter { m -> m.isSupportedTexture() }
            .map { m ->
                val buffer = m.source!!.bufferView!!
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
                    id = ids.get(m),
                    data = result,
                    width = decoder.width,
                    height = decoder.height,
                    hasAlpha = decoder.hasAlpha()
                )
            }.map {
                it.id to it
            }.toMap()
    }
}
