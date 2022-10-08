package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfAsset
import com.curiouscreature.kotlin.math.Mat4
import com.dwursteisen.gltf.parser.sprite.internal.AsepriteDataModel
import com.dwursteisen.minigdx.scene.api.model.Position
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions
import org.opentest4j.AssertionFailedError
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.reflect.KProperty

class GltfDelegate(resourceName: String) {

    private val asset by lazy {
        val resource = GltfDelegate::class.java.getResource(resourceName) ?: throw IllegalArgumentException(
            "$resourceName is not a valid Gltf file."
        )

        val path = File(resource.toURI())
        GltfAsset.fromFile(path.absolutePath)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): GltfAsset {
        return asset
    }
}

fun gltf(resourceName: String): GltfDelegate {
    return GltfDelegate(resourceName)
}

data class GlftRessource(val asset: GltfAsset, val path: File)
fun gltfResource(resourceName: String): GlftRessource {
    val resource = GltfDelegate::class.java.getResource(resourceName) ?: throw IllegalArgumentException(
        "$resourceName is not a valid Gltf file."
    )
    val path = File(resource.toURI())
    val asset = GltfAsset.fromFile(path.absolutePath)
    return GlftRessource(asset, path)
}

fun aseprite(resourceName: String): Pair<AsepriteDataModel, File> {
    val resource = GltfDelegate::class.java.getResource(resourceName) ?: throw IllegalArgumentException(
        "$resourceName is not a valid Aseprite JSON file."
    )

    return ObjectMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .readValue(resource, AsepriteDataModel::class.java) to File(resource.toURI())
}

fun assertPositionEquals(expected: Position, actual: Position) {
    Assertions.assertEquals(expected.x, actual.x, 0.001f)
    Assertions.assertEquals(expected.y, actual.y, 0.001f)
    Assertions.assertEquals(expected.z, actual.z, 0.001f)
}

fun assertMat4Equals(expected: Mat4, actual: Mat4) {
    try {
        val array = actual.asGLArray()
        expected.asGLArray().forEachIndexed { i, value ->
            Assertions.assertEquals(value, array[i], 0.001f)
        }
    } catch (ex: AssertionFailedError) {
        throw AssertionFailedError(
            """expected: 
            |$expected 
            |but was: 
            |$actual.
            |
            |
            |expected position: ${expected.position}
            |actual position: ${actual.position}
            |
            |expected rotation: ${expected.rotation}
            |actual rotation: ${actual.rotation}
            |
            |expected scale: ${expected.scale}
            |actual scale: ${actual.scale}
        """.trimMargin(),
            expected,
            actual
        )
    }
}
