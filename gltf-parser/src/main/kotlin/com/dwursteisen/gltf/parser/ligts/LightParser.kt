package com.dwursteisen.gltf.parser.ligts

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.beust.klaxon.JsonObject
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.model.Color
import com.dwursteisen.minigdx.scene.api.model.Position

class LightParser(private val gltfAsset: GltfAsset) {

    private fun toPointLight(node: GltfNode, obj: JsonObject): PointLight {
        val colors = obj.array<Int>("color")?.value ?: mutableListOf(0, 0, 0)
        val intensity = obj.int("intensity") ?: 0
        val name = obj.string("name") ?: ""

        return PointLight(
            color = Color(
                colors[0] / 255f,
                colors[1] / 255f,
                colors[2] / 255f,
                1f
            ),
            name = name,
            intensity = intensity,
            position = Position(
                node.translation.x,
                node.translation.y,
                node.translation.z
            )
        )
    }

    fun pointLights(): Map<String, PointLight> {
        return extractLight("point", ::toPointLight)
    }

    private fun <T> extractLight(type: String, mapper: (GltfNode, JsonObject) -> T): Map<String, T> {
        val lightsExtension = gltfAsset.extensions["KHR_lights_punctual"] ?: return emptyMap()
        val lights = (lightsExtension as? JsonObject)?.array<JsonObject>("lights") ?: return emptyMap()
        val lightsStructure = lights.mapChildrenObjectsOnly { it }.value

        val nodesWithLight = gltfAsset.nodes.filter { it.extensions?.containsKey("KHR_lights_punctual") == true }
            .map { it to (it.extensions!!.getValue("KHR_lights_punctual") as JsonObject).int("light")!! }
            .map { it.first to lightsStructure[it.second] }
            .filter { it.second.string("type") == type }
            .toMap()

        val n = nodesWithLight.keys
        return gltfAsset.nodes
            .flatMap { p -> p.children?.map { p to it } ?: emptyList() }
            .filter { n.contains(it.second) }
            .map { (it.first.name ?: "") to mapper(it.first, nodesWithLight.getValue(it.second)) }
            .toMap()
    }
}
