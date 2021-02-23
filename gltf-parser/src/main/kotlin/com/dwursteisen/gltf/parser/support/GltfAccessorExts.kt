package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfAccessor
import com.adrienben.tools.gltf.models.GltfComponentType
import com.adrienben.tools.gltf.models.GltfType
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

fun GltfAccessor?.toChunckedFloat(): List<List<Float>> {
    this ?: return emptyList()

    return toFloatArray()
        .toList()
        .chunked(geChunckSize())
}

fun GltfAccessor?.toChunckedInt(): List<List<Int>> {
    this ?: return emptyList()

    return toIntArray()
        .toList()
        .chunked(geChunckSize())
}

private fun GltfAccessor.geChunckSize(): Int {
    val chunkSize = when (this.type) {
        GltfType.SCALAR -> 1
        GltfType.VEC2 -> 2
        GltfType.VEC3 -> 3
        GltfType.VEC4 -> 4
        GltfType.MAT2 -> 4
        GltfType.MAT3 -> 9
        GltfType.MAT4 -> 16
    }
    return chunkSize
}

fun GltfAccessor?.toIntArray(): IntArray {
    if (this == null) {
        return intArrayOf()
    }
    if (componentType != GltfComponentType.UNSIGNED_SHORT && componentType != GltfComponentType.UNSIGNED_BYTE) {
        throw IllegalStateException("The component type is '$componentType'. Expected Short or Byte instead.")
    }

    val reader: (stream: LittleEndien) -> Short = if (componentType == GltfComponentType.UNSIGNED_SHORT) {
        { stream: LittleEndien -> stream.readShort() }
    } else {
        { stream: LittleEndien -> stream.readByte().toShort() }
    }
    return bufferView?.let {
        val data = it.buffer.data
        val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
        val shorts = mutableListOf<Short>()
        while (stream.available() > 0) {
            shorts.add(reader(stream))
        }
        shorts.map { it.toInt() }.toIntArray()
    } ?: intArrayOf()
}

