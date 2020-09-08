package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.inverse
import com.curiouscreature.kotlin.math.rotation
import com.dwursteisen.gltf.parser.armature.ArmatureParser
import com.dwursteisen.gltf.parser.camera.CameraParser
import com.dwursteisen.gltf.parser.lights.LightParser
import com.dwursteisen.gltf.parser.material.MaterialParser
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.isBox
import com.dwursteisen.gltf.parser.support.transformation
import com.dwursteisen.minigdx.scene.api.Scene
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import com.dwursteisen.minigdx.scene.api.relation.Node
import com.dwursteisen.minigdx.scene.api.relation.ObjectType

class SceneParser(private val gltfAsset: GltfAsset) {

    private val ids: Dictionary = Dictionary()

    private val cameras = CameraParser(gltfAsset, ids)

    private val models = ModelParser(gltfAsset, ids)

    private val materials = MaterialParser(gltfAsset, ids)

    private val lights = LightParser(gltfAsset, ids)

    private val armatures = ArmatureParser(gltfAsset, ids)

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

    private fun GltfNode.toNode(ids: Dictionary): List<Node> {
        return when {
            // Model
            mesh != null -> listOf(createModelNode(ids, this))
            // Camera
            children?.any { it.camera != null } == true -> listOf(createCamera(ids, this))
            // Light
            extensions?.containsKey("KHR_lights_punctual") == true -> emptyList()
            // Armature
            children?.any { it.skin != null} == true -> listOf(createArmature(ids, this))
            // Box
            isBox -> listOf(createBoxNode(ids, this))
            else -> emptyList()
        }
    }

    private fun createArmature(ids: Dictionary, node: GltfNode): Node {
        val skin = node.children!!.first { it.skin != null }
        return Node(
            reference = ids.get(skin),
            name = node.name ?: "",
            type = ObjectType.ARMATURE,
            transformation = Transformation(node.transformation.asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids) } ?: emptyList()
        )
    }

    private fun createCamera(ids: Dictionary, node: GltfNode): Node {
        val camera = node.children!!.first { it.camera != null }
        val id: Id = ids.get(camera)
        val transformation = node.transformation *
                rotation(
                    Float3(
                        1f,
                        0f,
                        0f
                    ), -90f
                )
        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.CAMERA,
            transformation = Transformation(inverse(transformation).asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(ids) } ?: emptyList()
        )
    }
    private fun createBoxNode(ids: Dictionary, node: GltfNode): Node {
        val id: Id = ids.get(node)
        return Node(
            reference = id,
            name = node.name ?: "",
            type = ObjectType.BOX,
            transformation = Transformation(node.transformation.asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(ids) } ?: emptyList()
        )
    }

    private fun createModelNode(ids: Dictionary, node: GltfNode): Node {
        return Node(
            reference = ids.get(node.mesh!!),
            name = node.name ?: "",
            type = ObjectType.MODEL,
            transformation = Transformation(node.transformation.asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode(this.ids) } ?: emptyList()
        )
    }
}

