package com.dwursteisen.gltf.parser.scene

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.dwursteisen.gltf.parser.armature.ArmatureParser
import com.dwursteisen.gltf.parser.camera.CameraParser
import com.dwursteisen.gltf.parser.ligts.LightParser
import com.dwursteisen.gltf.parser.material.MaterialParser
import com.dwursteisen.gltf.parser.model.ModelParser
import com.dwursteisen.gltf.parser.support.isBox
import com.dwursteisen.gltf.parser.support.transformation
import com.dwursteisen.minigdx.scene.api.Scene
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.common.Transformation
import com.dwursteisen.minigdx.scene.api.relation.Node
import com.dwursteisen.minigdx.scene.api.relation.ObjectType

class SceneParser(private val gltfAsset: GltfAsset) {

    private val cameras = CameraParser(gltfAsset)

    private val models = ModelParser(gltfAsset)

    private val materials = MaterialParser(gltfAsset)

    private val lights = LightParser(gltfAsset)

    private val armatures = ArmatureParser(gltfAsset)

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
            children = gltfAsset.scene?.nodes?.flatMap { gltfNode -> gltfNode.toNode() } ?: emptyList()
        )
    }

    private fun GltfNode.toNode(): List<Node> {
        return when {
            // Model
            mesh != null -> listOf(createModelNode(this))
            // Camera
            camera != null -> emptyList()
            // Light
            extensions?.containsKey("KHR_lights_punctual") == true -> emptyList()
            isBox -> listOf(createBoxNode(this))
            else -> emptyList()
        }
    }

    private fun createBoxNode(node: GltfNode): Node {
        val id: Id = gltfAsset.nodes.filter { it.isBox }
            .mapIndexed { index, gltfNode -> index to gltfNode }
            .first { it.second == node }
            .first

        return Node(
            reference = id,
            type = ObjectType.BOX,
            transformation = Transformation(node.transformation.asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode() } ?: emptyList()
        )
    }

    private fun createModelNode(node: GltfNode): Node {
        val id: Id = gltfAsset.nodes.filter { it.mesh != null }
            .mapIndexed { index, gltfNode -> index to gltfNode }
            .first { it.second.mesh == node.mesh }
            .first

        return Node(
            reference = id,
            type = ObjectType.MODEL,
            transformation = Transformation(node.transformation.asGLArray().toFloatArray()),
            children = node.children?.flatMap { gltfNode -> gltfNode.toNode() } ?: emptyList()
        )
    }
}
