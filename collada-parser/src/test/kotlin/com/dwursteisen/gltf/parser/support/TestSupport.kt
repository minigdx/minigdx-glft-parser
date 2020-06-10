package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfAsset
import com.curiouscreature.kotlin.math.Mat4
import org.junit.jupiter.api.Assertions
import org.opentest4j.AssertionFailedError
import java.io.File
import kotlin.reflect.KProperty

class GltfDelegate(resourceName: String) {

    private val asset by lazy {
        val path = File(GltfDelegate::class.java.getResource(resourceName).toURI())
        GltfAsset.fromFile(path.absolutePath)!!
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): GltfAsset {
        return asset
    }
}

fun gltf(resourceName: String): GltfDelegate {
    return GltfDelegate(resourceName)
}

fun assertMat4Equals(expected: Mat4, actual: Mat4) {
    try {
        val array = actual.toArray()
        expected.toArray().forEachIndexed { i, value ->
            Assertions.assertEquals(value, array[i], 0.001f)
        }
    } catch (ex: AssertionFailedError) {
        throw AssertionFailedError("""expected: $expected but was: $actual.
            |
            |expected position: ${expected.position}
            |actual position: ${actual.position}
            |
            |expected rotation: ${expected.rotation}
            |actual rotation: ${actual.rotation}
            |
            |expected position: ${expected.scale}
            |actual position: ${actual.scale}
        """.trimMargin(), expected, actual)
    }
}

