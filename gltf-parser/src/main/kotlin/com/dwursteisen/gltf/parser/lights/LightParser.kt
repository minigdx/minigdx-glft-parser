package com.dwursteisen.gltf.parser.lights

import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfLightPunctualExtension
import com.adrienben.tools.gltf.models.GltfLightType
import com.adrienben.tools.gltf.models.GltfNode
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.minigdx.scene.api.common.Id
import com.dwursteisen.minigdx.scene.api.light.Light
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.model.Color

class LightParser(private val gltfAsset: GltfAsset, private val ids: Dictionary) {

    private fun toPointLight(node: GltfNode, obj: GltfLightPunctualExtension): PointLight {
        obj as GltfLightPunctualExtension.GltfPointLight
        return PointLight(
            id = ids.get(node),
            color = Color(
                obj.color.r,
                obj.color.g,
                obj.color.b,
                obj.color.a
            ),
            name = obj.name,
            intensity = obj.intensity.toInt()
        )
    }

    fun pointLights(): Map<Id, PointLight> {
        return extractLight(GltfLightType.POINT, ::toPointLight)
    }

    private fun <T : Light> extractLight(
        type: GltfLightType,
        mapper: (GltfNode, GltfLightPunctualExtension) -> T
    ): Map<Id, T> {
        val lightsExtension = gltfAsset.extensions?.lightsPunctual ?: return emptyMap()
        val filtered = lightsExtension.mapIndexed { index, light ->  index to light }
            .filter { (_, light) -> light.type == type }
            .toMap()

        val nodesWithLightReference = gltfAsset.nodes.mapNotNull { node ->
            val ref = node.extensions?.get(EXTENSION_NAME) as? Map<String, Int>
            ref?.get("light")?.let { it to node }
        }.toMap()

        return filtered.map { (index, light) -> mapper(nodesWithLightReference[index]!!, light) }
            .map { it.id to it }
            .toMap()
    }

    companion object {
        private const val EXTENSION_NAME = "KHR_lights_punctual"
    }
}
