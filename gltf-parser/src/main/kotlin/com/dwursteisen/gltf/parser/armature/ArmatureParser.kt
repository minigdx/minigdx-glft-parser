package com.dwursteisen.gltf.parser.armature

import com.adrienben.tools.gltf.models.GltfAnimation
import com.adrienben.tools.gltf.models.GltfAnimationTargetPath
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfChannel
import com.adrienben.tools.gltf.models.GltfNode
import com.adrienben.tools.gltf.models.GltfSkin
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.Quaternion
import com.curiouscreature.kotlin.math.scale
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.gltf.parser.support.Dictionary
import com.dwursteisen.gltf.parser.support.fromTransformation
import com.dwursteisen.gltf.parser.support.toFloatArray
import com.dwursteisen.minigdx.scene.api.armature.Animation
import com.dwursteisen.minigdx.scene.api.armature.Armature
import com.dwursteisen.minigdx.scene.api.armature.Frame
import com.dwursteisen.minigdx.scene.api.armature.Joint
import com.dwursteisen.minigdx.scene.api.common.Id

typealias KeyFrame = Pair<Float, Mat4>
typealias GltfIndex = Int
typealias Timestamp = Float

class ArmatureParser(private val gltf: GltfAsset, private val ids: Dictionary) {

    private fun GltfSkin.toArmature(): Armature {
        val matrices = inverseBindMatrices.toFloatArray()
            .toList()
            .chunked(16)

        val joints = this.joints.mapIndexed { index, gltfNode ->
            Joint(
                name = gltfNode.name ?: "",
                inverseGlobalTransformation = fromTransformation(Mat4.fromColumnMajor(*matrices[index].toFloatArray()))
            )
        }

        return Armature(
            id = ids.get(this),
            name = name ?: "",
            joints = joints.toTypedArray()
        )
    }

    fun armatures(): Map<Id, Armature> {
        val skins = gltf.skin ?: emptyList()
        return skins.map { skin -> skin.toArmature() }
            .map { it.id to it }
            .toMap()
    }

    fun animations(): Map<Id, List<Animation>> {
        return gltf.animations.map { it.toAnimation() }
            .flatten()
            .groupBy { it.armatureId }
    }

    private fun GltfChannel.convert(chunk: Int, toMat: (List<Float>) -> Mat4): List<KeyFrame> {
        val times = this.sampler.input.toFloatArray()
            .toList()
        val values = this.sampler.output.toFloatArray()
            .toList()
            .chunked(chunk)

        return times.zip(values) { t, v ->
            t to toMat(v)
        }
    }

    // should be called for one node!
    private fun List<GltfChannel>.toKeyframes(): List<KeyFrame> {
        val byTargets = this.groupBy { it.target.path }

        val translations = byTargets[GltfAnimationTargetPath.TRANSLATION]?.first()?.convert(3) { values ->
            translation(
                Float3(
                    values[0],
                    values[1],
                    values[2]
                )
            )
        }?.toMap() ?: emptyMap()

        val rotations = byTargets[GltfAnimationTargetPath.ROTATION]?.first()?.convert(4) { values ->
            Mat4.from(
                Quaternion(
                    values[0],
                    values[1],
                    values[2],
                    values[3]
                )
            )
        }?.toMap() ?: emptyMap()

        val scales = byTargets[GltfAnimationTargetPath.SCALE]?.first()?.convert(3) { values ->
            scale(
                Float3(
                    values[0],
                    values[1],
                    values[2]
                )
            )
        }?.toMap() ?: emptyMap()

        val timings = (translations.keys + rotations.keys + scales.keys).toList().sorted()

        var lastRotation = Mat4.identity()
        var lastScale = Mat4.identity()
        var lastTranslation = Mat4.identity()
        return timings.map {
            val t = translations.getOrDefault(it, lastTranslation)
            val r = rotations.getOrDefault(it, lastRotation)
            val s = scales.getOrDefault(it, lastScale)

            lastTranslation = t
            lastRotation = r
            lastScale = s

            val transformation = t * r * s
            it to transformation
        }
    }

    private fun GltfAnimation.toAnimation(): List<Animation> {
        return gltf.skin?.map { skin ->
            val channels = this.channels.filter { skin.joints.contains(it.target.node) }
            val transformations: Map<Int, List<KeyFrame>> = channels.groupBy { it.target.node!!.index }
                .mapValues { it.value.toKeyframes() }
            val byTime: Map<Float, Map<GltfIndex, Mat4>> = transformations.flatMap { (index, frames) ->
                frames.map { it.first to (index to it.second) }
            }.groupBy { it.first }
                .mapValues { it.value.map { it.second }.toMap() }

            val animation = byTime.mapValues { (_, localTransforms) ->
                skin.toFrames(localTransforms)
            }
            Animation(
                id = ids.get(this),
                armatureId = ids.get(skin),
                name = name ?: "",
                duration = animation.keys.maxOrNull() ?: 0f,
                frames = animation.map { (time, globalTransformations) ->
                    Frame(
                        time = time,
                        globalTransformations = globalTransformations.map { fromTransformation(it) }.toTypedArray()
                    )
                }
            )
        } ?: emptyList()
    }

    /**
     * Transform all local transformation into global transformations
     */
    private fun GltfSkin.toFrames(localTransforms: Map<GltfIndex, Mat4>): List<Mat4> {
        val globals = mutableMapOf<GltfIndex, Mat4>()
        fun GltfNode.traverse(parent: Mat4 = Mat4.identity()) {
            val global = parent * localTransforms[index]!!
            globals[index] = global
            children?.forEach { it.traverse(global) }
        }

        val filterIndex = joints.flatMap { it.children ?: emptyList() }
            .map { it.index }

        joints.filter { !filterIndex.contains(it.index) }
            .forEach { root -> root.traverse() }

        return this.joints.map { node ->
            globals.getValue(node.index)
        }
    }
}
