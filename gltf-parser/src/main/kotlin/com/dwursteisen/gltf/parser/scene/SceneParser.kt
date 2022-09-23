package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.rotation
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

class SceneParser(private val gltfAsset: GltfAsset) {

    private val ids: Dictionary = Dictionary()

    private val cameras = CameraParser(gltfAsset, ids)

    private val models = ModelParser(gltfAsset, ids)

    private val materials = MaterialParser(gltfAsset, ids)

    private val lights = LightParser(gltfAsset, ids)

    private val armatures = ArmatureParser(gltfAsset, ids)

    @ExperimentalSerializationApi
    fun parse(): Scene {
        return Scene(
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
            children?.any { it.camera != null } == true -> listOf(createCamera(ids, this, alteration))
            // Light
            children?.firstOrNull()?.extensions?.containsKey("KHR_lights_punctual") == true -> listOf(createLight(ids, this, alteration))
            // Armature
            children?.any { it.skin != null } == true -> listOf(createArmature(ids, this, alteration))
            // Box
            isBox -> listOf(createBoxNode(ids, this, alteration))
            else -> emptyList()
        }
    }

    private fun createLight(ids: Dictionary, node: GltfNode, alteration: Mat4): Node {
        val light = node.children!!.first()
        val id: Id = ids.get(light)
        val transformation = fromTransformation(alteration * node.transformation.combined)
        return Node(
            reference = id,
            name = light.name ?: "",
            type = ObjectType.LIGHT,
            transformation = transformation,
            children = light.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids, alteration) } ?: emptyList(),
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
        val camera = node.children!!.first { it.camera != null }
        val id: Id = ids.get(camera.camera!!)
        val correction = rotation(
            Float3(
                1f,
                0f,
                0f
            ),
            -90f
        )

        val transformation = fromTransformation(alteration * node.transformation.combined * correction)

        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.CAMERA,
            transformation = transformation,
            // Add an alteration so children will be placed correctly regarding the camera
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(ids, alteration * rotation(Float3(90f, 0f, 0f))) } ?: emptyList(),
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

    private fun convertExtras(extras: Map<kotlin.String, kotlin.Any?>?): Map<String, String> {
        extras ?: return emptyMap()
        return extras.mapNotNull { (key, value) ->
            if (value == null) {
                null
            } else {
                key to value.toString()
            }
        }.toMap()
    }
}
