package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfAccessor
import com.adrienben.tools.gltf.models.GltfComponentType
import com.jme3.util.LittleEndien
import java.io.ByteArrayInputStream

fun GltfAccessor?.toFloatArray(): FloatArray {
    if (this == null) {
        return floatArrayOf()
    }
    if (componentType != GltfComponentType.FLOAT) {
        throw IllegalStateException("The component type is '$componentType'. Expected Float instead.")
    }

    return bufferView?.let {
        val data = it.buffer.data
        val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
        val floats = mutableListOf<Float>()
        while (stream.available() > 0) {
            floats.add(stream.readFloat())
        }
        floats.toFloatArray()
    } ?: floatArrayOf()
}

fun GltfAccessor?.toIntArray(): IntArray {
    if (this == null) {
        return intArrayOf()
    }
    if (componentType != GltfComponentType.UNSIGNED_SHORT && componentType != GltfComponentType.UNSIGNED_BYTE) {
        throw IllegalStateException("The component type is '$componentType'. Expected Short or Byte instead.")
    }

    return bufferView?.let {
        val data = it.buffer.data
        val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
        val shorts = mutableListOf<Short>()
        while (stream.available() > 0) {
            shorts.add(stream.readShort())
        }
        shorts.map { it.toInt() }.toIntArray()
    } ?: intArrayOf()
}

