package com.dwursteisen.gltf.parser.armature

import collada.Transformation
import com.adrienben.tools.gltf.models.*
import com.curiouscreature.kotlin.math.*
import com.dwursteisen.gltf.parser.support.toFloatArray
import com.dwursteisen.gltf.parser.support.transformation
import com.dwursteisen.minigdx.scene.api.armature.Animation
import com.dwursteisen.minigdx.scene.api.armature.Armature
import com.dwursteisen.minigdx.scene.api.armature.Frame
import com.dwursteisen.minigdx.scene.api.armature.Joint

typealias KeyFrame = Pair<Float, Mat4>

class ArmatureParser(private val gltf: GltfAsset) {

    private fun GltfSkin.toArmature(): Armature {
        fun convert(j: GltfNode, parentGlobalTransformation: Mat4 = Mat4.identity()): Joint {
            val globalTransformation = parentGlobalTransformation * j.transformation
            return Joint(
                id = j.index,
                name = j.name ?: "",
                childs = j.children?.map { convert(it, globalTransformation) } ?: emptyList(),
                inverseGlobalTransformation = Transformation(inverse(globalTransformation).asGLArray().toFloatArray())
            )
        }

        return Armature(
            id = index,
            name = name ?: "",
            rootJoint = convert(this.joints.first())
        )
    }

    fun armatures(): Map<Int, Armature> {
        val skins = gltf.skin ?: emptyList()
        return skins.map { it.toArmature() }
            .map { it.id to it }
            .toMap()
    }

    fun animations(): Map<String, Animation> {
        return gltf.animations.mapIndexed { index, it ->
            it.toAnimation(index)
        }.map {
            it.name to it
        }.toMap()
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

    private fun GltfAnimation.toAnimation(animationIndex: Int): Animation {
        val byNode = this.channels
            .filter { it.target.node == null }
            .groupBy { it.target.node!! }
            .mapValues { channels.toKeyframes() }

        val frames = byNode.flatMap { (node, keyframe) ->
            keyframe.map {
                Frame(
                    time = it.first,
                    jointId = node.index,
                    localTransformation = Transformation(it.second.asGLArray().toFloatArray())
                )
            }
        }

        return Animation(
            id = animationIndex,
            name = name ?: "",
            duration = frames.map { it.time }.max() ?: 0f,
            frames = frames
        )
    }
}
