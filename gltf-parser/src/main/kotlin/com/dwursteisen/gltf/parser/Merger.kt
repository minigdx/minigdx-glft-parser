package com.dwursteisen.gltf.parser

import com.dwursteisen.minigdx.scene.api.Scene
import java.io.File

class Merger(private val inputs: List<File>) {

    @ExperimentalStdlibApi
    fun mergeAll(output: File) {
        val scenes = inputs.map {
            if (it.extension == "protobuf") {
                Scene.readProtobuf(output.readBytes())
            } else {
                Scene.readJson(output.readBytes())
            }
        }

        val mergedScene = Scene(
            perspectiveCameras = merge(scenes) { it.perspectiveCameras },
            orthographicCameras = merge(scenes) { it.orthographicCameras },
            models = merge(scenes) { it.models },
            materials = merge(scenes) { it.materials },
            pointLights = merge(scenes) { it.pointLights },
            armatures = merge(scenes) { it.armatures },
            animations = merge(scenes) { it.animations },
            boxes = merge(scenes) { it.boxes },
            children = scenes.flatMap { it.children },
            sprites = merge(scenes) { it.sprites }
        )

        val byteArray = Scene.writeProtobuf(mergedScene)
        output.writeBytes(byteArray)
    }

    private fun <A, B> merge(scenes: List<Scene>, extractor: (Scene) -> Map<A, B>): Map<A, B> {
        return scenes.map { extractor(it) }
            .flatMap { it.entries }
            .map { (key, value) -> key to value }
            .toMap()
    }
}
