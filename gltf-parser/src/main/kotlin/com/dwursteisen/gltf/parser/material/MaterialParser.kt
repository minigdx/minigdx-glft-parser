package com.dwursteisen.gltf.parser.material

import com.adrienben.tools.gltf.models.GltfAsset
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.isSupportedTexture
import com.dwursteisen.gltf.parser.support.source
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.material.Material
import de.matthiasmann.twl.utils.PNGDecoder
import java.io.ByteArrayInputStream
import java.io.File
import java.util.Base64

class MaterialParser(
    private val rootPath: File,
    private val gltfAsset: GltfAsset,
    private val ids: Dictionary
) {

    fun materials(): Map<Id, Material> {
        return gltfAsset.materials
            // keep only materials using texture
            .filter { m -> m.isSupportedTexture() }
            .map { m ->

                val (data, uri, isExternal) = if (m.source!!.bufferView != null) {
                    // Get data from the buffer
                    val buffer = m.source!!.bufferView!!
                    val data = buffer.buffer.data.copyOfRange(buffer.byteOffset, buffer.byteOffset + buffer.byteLength)
                    Triple(data, null, false)
                } else {
                    // Get the data from the file if the texture is external.
                    val data = rootPath.parentFile.resolve(m.source!!.uri!!).readBytes()
                    Triple(data, m.source!!.uri!!, true)
                }

                // Read the PNG to know if some alpha are used in the texture.
                val decoder = PNGDecoder(ByteArrayInputStream(data))

                Material(
                    name = m.name ?: "",
                    id = ids.get(m),
                    data = if (!isExternal) {
                        Base64.getEncoder().encode(data)
                    } else {
                        ByteArray(0)
                    },
                    width = decoder.width,
                    height = decoder.height,
                    hasAlpha = decoder.hasAlpha(),
                    uri = uri,
                    isExternal = isExternal,
                )
            }.associateBy {
                it.id
            }
    }
}
