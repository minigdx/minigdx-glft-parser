package com.dwursteisen.gltf.parser.lights

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfNode
import com.beust.klaxon.JsonObject
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.light.Light
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.model.Color

class LightParser(private val gltfAsset: GltfAsset, private val ids: Dictionary) {

    private fun toPointLight(node: GltfNode, obj: JsonObject): PointLight {
        val colors = obj.array<Int>("color")?.value ?: mutableListOf(0, 0, 0)
        val intensity = obj.int("intensity") ?: 0
        val name = obj.string("name") ?: ""

        return PointLight(
            id = ids.get(node),
            color = Color(
                colors[0] / 255f,
                colors[1] / 255f,
                colors[2] / 255f,
                1f
            ),
            name = name,
            intensity = intensity
        )
    }

    fun pointLights(): Map<Id, PointLight> {
        return extractLight("point", ::toPointLight)
    }

    private fun <T : Light> extractLight(type: String, mapper: (GltfNode, JsonObject) -> T): Map<Id, T> {
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
            .map { mapper(it.first, nodesWithLight.getValue(it.second)) }
            .map { it.id to it }
            .toMap()
    }
}
