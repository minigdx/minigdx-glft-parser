package collada

import com.adrienben.tools.gltf.models.GltfAccessor
import com.adrienben.tools.gltf.models.GltfAsset
import com.adrienben.tools.gltf.models.GltfComponentType
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


        val vertices = positions.zip(colors).zip(normals) { pair, normal ->
            val (position, color) = pair
            Vertex(
                position = position,
                color = color,
                normal = normal,
                influence = Influence()
            )
        }
        val indices = gltfMesh.indices.toIntArray()

        val mesh = Mesh(
            vertices = vertices,
            verticesOrder = indices
        )
        return Model(
            mesh = mesh,
            armature = EmptyArmature,
            animations = EmptyAnimations
        )
    }

    private fun GltfAccessor?.toIntArray(): IntArray {
        if(this == null) {
            return intArrayOf()
        }
        if (componentType != GltfComponentType.UNSIGNED_SHORT) {
            throw IllegalStateException("The component type is '$componentType'. Expected Float instead.")
        }

        return bufferView?.let {
            val data = it.buffer.data
            val stream = LittleEndien(ByteArrayInputStream(data, it.byteOffset, it.byteLength))
            val shorts = mutableListOf<Short>()
            while(stream.available() > 0) {
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
            while(stream.available() > 0) {
                floats.add(stream.readFloat())
            }
            floats.toFloatArray()
        } ?: floatArrayOf()
    }
}
