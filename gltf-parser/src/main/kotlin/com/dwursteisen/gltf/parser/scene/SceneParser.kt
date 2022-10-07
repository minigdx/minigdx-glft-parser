package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Mat4
import com.dwursteisen.gltf.parser.armature.ArmatureParser
import com.dwursteisen.gltf.parser.camera.CameraParser
import com.dwursteisen.gltf.parser.lights.LightParser
import com.dwursteisen.gltf.parser.material.MaterialParser
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.combined
import com.dwursteisen.gltf.parser.support.fromTransformation
import com.dwursteisen.gltf.parser.support.isBox
import com.dwursteisen.gltf.parser.support.transformation
import com.dwursteisen.minigdx.scene.api.Scene
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.relation.Node
import com.dwursteisen.minigdx.scene.api.relation.ObjectType
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import java.lang.IllegalStateException

class SceneParser(rootPath: File, private val gltfAsset: GltfAsset) {

    private val ids: Dictionary = Dictionary()

    private val cameras = CameraParser(gltfAsset, ids)

    private val models = ModelParser(gltfAsset, ids)

    private val materials = MaterialParser(rootPath, gltfAsset, ids)

    private val lights = LightParser(gltfAsset, ids)

    private val armatures = ArmatureParser(gltfAsset, ids)

    @ExperimentalSerializationApi
    fun parse(): Scene {
        // Minimal Blender exporter version
        if (extractVersion(gltfAsset.asset.generator) < MIN_VERSION) {
            throw IllegalStateException(
                "The generator used ('${gltfAsset.asset.generator}') " +
                    "for the gltf file is not supported. The minimal version expected is '$MIN_VERSION'." +
                    "Please use the last Blender version and export your assert in gltf again."
            )
        }

        return Scene(
            generatorVersion = this::class.java.`package`.implementationVersion ?: "Unknown Version",
            perspectiveCameras = cameras.perspectiveCameras(),
            orthographicCameras = cameras.orthographicCameras(),
            models = models.objects(),
            materials = materials.materials(),
            pointLights = lights.pointLights(),
            armatures = armatures.armatures(),
            animations = armatures.animations(),
            boxes = models.boxes(),
            children = gltfAsset.scene?.nodes?.flatMap { gltfNode -> gltfNode.toNode(ids) } ?: emptyList()
        )
    }

    @ExperimentalSerializationApi
    private fun GltfNode.toNode(ids: Dictionary, alteration: Mat4 = Mat4.identity()): List<Node> {
        return when {
            // Model
            mesh != null -> listOf(createModelNode(ids, this, alteration))
            // Camera
            camera != null -> listOf(createCamera(ids, this, alteration))
            // Light
            extensions?.containsKey("KHR_lights_punctual") == true -> listOf(createLight(ids, this, alteration))
            // Armature
            children?.any { it.skin != null } == true -> listOf(createArmature(ids, this, alteration))
            // Box
            isBox -> listOf(createBoxNode(ids, this, alteration))
            else -> emptyList()
        }
    }

    private fun createLight(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val id: Id = ids.get(node)
        val transformation = fromTransformation(alteration * node.transformation.combined)
        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.LIGHT,
            transformation = transformation,
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids, alteration) } ?: emptyList(),
            customProperties = convertExtras(node.extras)
        )
    }

    @ExperimentalSerializationApi
    private fun createArmature(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val skin = node.children!!.first { it.skin != null }
        val transformation = fromTransformation(alteration * node.transformation.combined)
        return Node(
            reference = ids.get(skin.skin!!),
            name = node.name ?: "",
            type = ObjectType.ARMATURE,
            transformation = transformation,
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids, alteration) } ?: emptyList(),
            customProperties = convertExtras(node.extras)
        )
    }

    @ExperimentalSerializationApi
    private fun createCamera(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val id: Id = ids.get(node.camera!!)

        val transformation = fromTransformation(alteration * node.transformation.combined)

        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.CAMERA,
            transformation = transformation,
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(ids, alteration) } ?: emptyList(),
            customProperties = convertExtras(node.extras)
        )
    }

    @ExperimentalSerializationApi
    private fun createBoxNode(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val id: Id = ids.get(node)
        val transformation = fromTransformation(alteration * node.transformation.combined)
        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.BOX,
            transformation = transformation,
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(ids, alteration) } ?: emptyList(),
            customProperties = convertExtras(node.extras)
        )
    }

    @ExperimentalSerializationApi
    private fun createModelNode(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val transformation = fromTransformation(alteration * node.transformation.combined)
        return Node(
            reference = ids.get(node.mesh!!),
            name = node.name ?: "",
            type = ObjectType.MODEL,
            transformation = transformation,
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids, alteration) } ?: emptyList(),
            customProperties = convertExtras(node.extras)
        )
    }

    private fun convertExtras(extras: Map<String, Any?>?): Map<String, String> {
        extras ?: return emptyMap()
        return extras.mapNotNull { (key, value) ->
            if (value == null) {
                null
            } else {
                key to value.toString()
            }
        }.toMap()
    }

    companion object {
        internal fun extractVersion(generatorVersion: String?): String {
            val (version) = generatorVersion?.split(" ")?.takeLast(1) ?: listOf("")
            return version
        }

        private const val MIN_VERSION = "v3.3.27"
    }
}
