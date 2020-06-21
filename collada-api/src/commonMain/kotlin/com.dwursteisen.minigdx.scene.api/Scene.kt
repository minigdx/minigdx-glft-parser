package com.dwursteisen.minigdx.scene.api

import collada.Transformation
import com.dwursteisen.minigdx.scene.api.armature.Animation
import com.dwursteisen.minigdx.scene.api.armature.Armature
import com.dwursteisen.minigdx.scene.api.armature.Joint
import com.dwursteisen.minigdx.scene.api.camera.Camera
import com.dwursteisen.minigdx.scene.api.camera.OrthographicCamera
import com.dwursteisen.minigdx.scene.api.camera.PerspectiveCamera
import com.dwursteisen.minigdx.scene.api.light.PointLight
import com.dwursteisen.minigdx.scene.api.material.Material
import com.dwursteisen.minigdx.scene.api.model.Model
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoId
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.inverse

class AnimationReference(
    val name: String,
    val armatureId: Int
)

class Keyframe(
    val time: Float,
    val pose: AnimatedJoint
)

class AnimatedJoint(
    val id: Int,
    val globalTransformation: Transformation,
    val children: List<AnimatedJoint>
)

@Serializable
data class Scene(
    @ProtoId(0)
    val perspectiveCameras: Map<String, Camera> = emptyMap(),
    @ProtoId(1)
    val orthographicCameras: Map<String, Camera> = emptyMap(),
    @ProtoId(2)
    val models: Map<String, Model> = emptyMap(),
    @ProtoId(3)
    val materials: Map<String, Material> = emptyMap(),
    @ProtoId(4)
    val pointLights: Map<String, PointLight> = emptyMap(),
    @ProtoId(5)
    val armatures: Map<Int, Armature> = emptyMap(),
    @ProtoId(6)
    private val animationsList: Map<String, Animation> = emptyMap()
) {

    private fun Joint.allJoints(): List<Joint> {
        return childs.flatMap { it.allJoints() } + this
    }

    private fun Joint.toPose(
        transformations: Map<Int, Mat4>,
        parentGlobalTransformation: Mat4 = Mat4.identity()
    ): AnimatedJoint {
        val globalTransformation = parentGlobalTransformation * transformations.getOrElse(this.id) {
            inverse(Mat4.fromColumnMajor(*inverseGlobalTransformation.matrix))
        }
        return AnimatedJoint(
            id = this.id,
            globalTransformation = Transformation(globalTransformation.asGLArray().toFloatArray()),
            children = this.childs.map { it.toPose(transformations, globalTransformation) }
        )
    }

    val animations: Map<AnimationReference, List<Keyframe>> by lazy {
        val jointsByArmature = armatures.entries.flatMap { it.value.rootJoint.allJoints().map { j -> j.id to it.key } }
            .toMap()

        val all: List<Pair<AnimationReference, List<Keyframe>>> = animationsList.flatMap { (name, animation) ->
            // For each animation, split it by armature
            val armatureAnimations = animation.frames
                .filter { jointsByArmature.containsKey(it.jointId) }
                .groupBy { jointsByArmature[it.jointId]!! }
            // Creation Pose for each time frame
            val animations = armatureAnimations.map { (armatureId, frames) ->
                // Group all transformations of this armature by time
                val transformationByTime = frames.groupBy { it.time }
                    .mapValues { entry -> entry.value.map { it.jointId to Mat4.fromColumnMajor(*it.localTransformation.matrix) }.toMap() }

                val reference = armatures[armatureId]!!

                // all poses for this animation, for this armature
                val poses = transformationByTime.map { (time, transformations) ->
                    Keyframe(
                        time = time,
                        pose = reference.rootJoint.toPose(transformations)
                    )
                }
                AnimationReference(
                    name = name,
                    armatureId = armatureId
                ) to poses
            }
            animations
        }
        all.toMap()
    }
    companion object {
        @ExperimentalStdlibApi
        fun readJson(data: ByteArray): Scene {
            val deserializer = Json(context = serialModule())
            return deserializer.parse(serializer(), data.decodeToString())
        }

        fun readProtobuf(data: ByteArray): Scene {
            val deserializer = ProtoBuf(context = serialModule())
            return deserializer.load(serializer(), data)
        }

        private fun serialModule(): SerialModule {
            return SerializersModule {
                polymorphic<Camera> {
                    PerspectiveCamera::class with PerspectiveCamera.serializer()
                    OrthographicCamera::class with OrthographicCamera.serializer()
                }
            }
        }

        @ExperimentalStdlibApi
        fun writeJson(model: Scene): ByteArray {
            val serializer = Json(context = serialModule())
            return serializer.stringify(serializer(), model).encodeToByteArray()
        }

        fun writeProtobuf(model: Scene): ByteArray {
            val serializer = ProtoBuf(context = serialModule())
            return serializer.dump(serializer(), model)
        }
    }
}
