package collada

import com.adrienben.tools.gltf.models.*
import com.curiouscreature.kotlin.math.*
import com.jme3.util.LittleEndien
import java.io.ByteArrayInputStream
import java.io.File

class GltfToMiniGdx(override val input: File) : Converter {

    override fun toProtobuf(output: File) {
        val gltf = GltfAsset.fromFile(input.absolutePath)
            ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
        val model = gltf.convertToModel()
        val data = Model.writeProtobuf(model)
        output.writeBytes(data)
    }

    @ExperimentalStdlibApi
    override fun toJson(output: File) {
        val gltf = GltfAsset.fromFile(input.absolutePath)
            ?: throw IllegalArgumentException("'$input' is not a valid gltf file")
        val model = gltf.convertToModel()
        val data = Model.writeJson(model)
        output.writeBytes(data)
    }

    private fun GltfAsset.convertToModel(): Model {
        // Support only one Mesh for the moment.
        // Only one primitive supported.
        // Will be, by default, in Triangles.
        val gltfMesh = this.meshes.first().primitives.first()
        val positions = gltfMesh.attributes["POSITION"].toFloatArray()
            .toList()
            .chunked(3)
            .map { Position(it[0], it[1], it[2]) }

        val colors = gltfMesh.attributes["COLOR_0"].toFloatArray()
            .toList()
            .chunked(4)
            .map { Color(it[0], it[1], it[2], it[3]) }
            .ifEmpty { positions.map { Color(1f, 0f, 0f, 1f) } }

        val normals = gltfMesh.attributes["NORMAL"].toFloatArray()
            .toList()
            .chunked(3)
            .map { Normal(it[0], it[1], it[2]) }

        val joints = gltfMesh.attributes["JOINTS_0"].toIntArray()
            .toList()
            .chunked(4)

        val weights = gltfMesh.attributes["WEIGHTS_0"].toFloatArray()
            .toList()
            .chunked(4)

        val boneIds = skin?.firstOrNull()?.joints?.mapIndexed { index, gltfNode ->
            index to (gltfNode.name ?: "")
        }?.toMap() ?: emptyMap()

        val influences = joints.zip(weights) { j, w ->
            val data = j.zip(w) { a, b ->
                if(b <= 0f) {
                    null
                } else {
                    InfluenceData(boneIds.getValue(a), b)
                }
            }
            Influence(data  = data.filterNotNull())
        }.ifEmpty { positions.map { Influence() } }

        val vertices = positions.zip(colors).zip(normals).zip(influences) { pair, influence ->
            val (positionAndColor, normal) = pair
            val (position, color) = positionAndColor
            Vertex(
                position = position,
                color = color,
                normal = normal,
                influence = influence
            )
        }
        val indices = gltfMesh.indices.toIntArray()

        val armature = skin?.firstOrNull().convertToArmature()
        val animations = animations.convertToAnimations()

        val mesh = Mesh(
            vertices = vertices,
            verticesOrder = indices
        )
        return Model(
            mesh = mesh,
            armature = armature,
            animations = animations
        )
    }

    private fun List<GltfAnimation>.convertToAnimations(): AnimationsDescription {
        if(this.isEmpty()) {
            return EmptyAnimations
        } else {
            val animations = this.map { animation ->

                val timings = mutableListOf<Float>()
                val joints = mutableListOf<String>()
                val translations = mutableMapOf<Float, MutableMap<String, Mat4>>()
                val rotations = mutableMapOf<Float, MutableMap<String, Mat4>>()
                val scales = mutableMapOf<Float, MutableMap<String, Mat4>>()

                animation.channels.forEach {channel ->
                    val timing = channel.sampler.input.toFloatArray().asList()
                    val target = when(channel.target.path) {
                        GltfAnimationTargetPath.TRANSLATION -> translations
                        GltfAnimationTargetPath.ROTATION -> rotations
                        GltfAnimationTargetPath.SCALE -> scales
                        else -> throw IllegalArgumentException("WEIGHT modificator not supported")
                    }

                    fun fromVec3(timing: List<Float>, accessor: GltfAccessor, transformation: (x: Float, y: Float, z: Float) -> Mat4): Map<Float, Mat4> {
                        return accessor.toFloatArray()
                            .toList()
                            .chunked(3)
                            .zip(timing) { vec3, time ->
                                val (x, y, z) = vec3
                                time to transformation(x, y, z)
                            }
                            .toMap()
                    }

                    fun fromVec4(timing: List<Float>, accessor: GltfAccessor, transformation: (x: Float, y: Float, z: Float, w: Float) -> Mat4): Map<Float, Mat4> {
                        return accessor.toFloatArray()
                            .toList()
                            .chunked(4)
                            .zip(timing) { vec4, time ->
                                val (x, y, z, w) = vec4
                                time to transformation(x, y, z, w)
                            }
                            .toMap()
                    }

                    val transformated = when(channel.target.path) {
                        GltfAnimationTargetPath.TRANSLATION -> fromVec3(timing, channel.sampler.output) { x, y, z -> translation(Float3(x, y, z))}
                        GltfAnimationTargetPath.ROTATION -> fromVec4(timing, channel.sampler.output) { x, y, z, w -> Mat4.from(Quaternion(x, y, z, w))}
                        GltfAnimationTargetPath.SCALE -> fromVec3(timing, channel.sampler.output) { x, y, z -> scale(Float3(x, y, z))}
                        else -> throw IllegalArgumentException("WEIGHT modificator not supported")
                    }

                    val jointName = channel.target.node?.name ?: ""
                    transformated.forEach { time, transformation ->
                        val keyframe = target.computeIfAbsent(time) { mutableMapOf() }
                        keyframe[jointName] = transformation
                    }

                    joints.add(jointName)
                    timings.addAll(timing)
                }

                val keyframes = timings.distinct().map {
                    val translation = translations[it]!!
                    val rotation = rotations[it]!!
                    val scale = scales[it]!!

                    val pose = joints.distinct().map {
                        val r = rotation[it] ?: Mat4.identity()
                        val s = scale[it] ?: Mat4.identity()
                        val t = translation[it] ?: Mat4.identity()
                        // FIXME: r * s * t
                        //val mat4 = r * s * t
                        val mat4 = t * r * s
                        it to transpose(mat4)
                    }.toMap()

                    it to pose
                }.toMap()

                Animation(
                    name = animation.name ?: "",
                    keyFrames = keyframes.map {
                        KeyFrame(
                            time = it.key,
                            transformations = it.value.mapValues { (_, mat4) -> Transformation(mat4.toFloatArray()) },
                            interpolation = "LINEAR"
                        )
                    }
                )
            }

            return Animations(
                animations = animations
            )
        }
    }

    private fun GltfSkin?.convertToArmature(): ArmatureDescription {
        // FIXME: regarder la structure de l'armature parce que c'est bizare.
        if (this == null) {
            return EmptyArmature
        }

        fun transformationMatrix(it: GltfNode): Mat4 {
            val scale = it.scale.let { scale(Float3(it.x, it.y, it.z)) }
            val translation = it.translation.let { translation(Float3(it.x, it.y, it.z)) }
            val rotation = it.rotation.let { Quaternion(it.i, it.j, it.k, it.a) }
                .let { Mat4.from(it) }


            // TODO: if not working -> transaction * scale * rotation
            // val transformation = rotation * scale * translation
            val transformation = translation * rotation * scale
            return transpose(transformation)
        }

        fun List<GltfNode>.convert(): List<Bone> = this.map {
            val transformation = transformationMatrix(it)
            Bone(
                id = it.name ?: "",
                transformation = Transformation(transformation.toFloatArray()),
                childs = it.children?.convert() ?: emptyList(),
                inverseBindPose = Transformation(Mat4.identity().toFloatArray())
            )
        }

        val rootBone = this.joints.convert().first()

        return Armature(rootBone)
    }

    private fun GltfAccessor?.toIntArray(): IntArray {
        if (this == null) {
            return intArrayOf()
        }
        if (componentType != GltfComponentType.UNSIGNED_SHORT) {
            throw IllegalStateException("The component type is '$componentType'. Expected Float instead.")
        }

        return bufferView?.let {
            val data = it.buffer.data
            val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
            val shorts = mutableListOf<Short>()
            while (stream.available() > 0) {
                shorts.add(stream.readShort())
            }
            shorts.map { it.toInt() }.toIntArray()
        } ?: intArrayOf()
    }

    private fun GltfAccessor?.toFloatArray(): FloatArray {
        if (this == null) {
            return floatArrayOf()
        }
        if (componentType != GltfComponentType.FLOAT) {
            throw IllegalStateException("The component type is '$componentType'. Expected Float instead.")
        }

        return bufferView?.let {
            val data = it.buffer.data
            val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
            val floats = mutableListOf<Float>()
            while (stream.available() > 0) {
                floats.add(stream.readFloat())
            }
            floats.toFloatArray()
        } ?: floatArrayOf()
    }
}
