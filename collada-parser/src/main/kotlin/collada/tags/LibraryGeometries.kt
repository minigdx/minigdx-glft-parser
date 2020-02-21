package collada.tags

import kotlin.math.max

class LibraryGeometries(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "geometry" -> Geometry(this).also { parserListener.startMesh(attributes.getValue("name").toString()) }
        else -> NopTagHandler(this)
    }

}

class Geometry(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "mesh" -> Mesh(this)
        else -> NopTagHandler(this)
    }

    override fun endParsing(): TagHandler {
        parserListener.endMesh()
        return super.endParsing()
    }
}

class Mesh(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "source" -> Source(this, attributes.getValue("id").toString())
        "triangles" -> Triangles(this)
        else -> NopTagHandler(this)
    }
}

class Source(override val parent: TagHandler, val id: String) : TagHandler {

    lateinit var floatArrayTag: FloatArrayTag
    lateinit var techniqueCommon: TechniqueCommon

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "float_array" -> FloatArrayTag(this).also { floatArrayTag = it }
        "technique_common" -> TechniqueCommon(this).also { techniqueCommon = it }
        else -> NopTagHandler(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun endParsing(): TagHandler {
        val data = techniqueCommon.factory.build(floatArrayTag.floatArray)
        if (id.contains("mesh-normals")) {
            parserListener.pushNormals(data as List<Vector3>)
        } else if (id.contains("mesh-positions")) {
            parserListener.pushPositions(data as List<Vector3>)
        } else if (id.contains("mesh-colors")) {
            parserListener.pushColors(data as List<Vector4>)
        }
        return parent
    }
}

class Triangles(override val parent: TagHandler) : TagHandler {

    var vertexIndex: Int = -1
    var normalIndex: Int = -1
    var textureIndex: Int = -1
    var colorIndex: Int = -1

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "input" -> Input(this).also {
            val semantic = attributes.get("semantic")
            when (semantic) {
                "VERTEX" -> vertexIndex = attributes.getValue("offset").toString().toInt()
                "NORMAL" -> normalIndex = attributes.getValue("offset").toString().toInt()
                "TEXCOORD" -> textureIndex = attributes.getValue("offset").toString().toInt()
                "COLOR" -> colorIndex = attributes.getValue("offset").toString().toInt()
            }
        }
        "p" -> PClass(this, vertexIndex, normalIndex, textureIndex, colorIndex)
        else -> NopTagHandler(this)
    }
}

class Input(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        else -> NopTagHandler(this)
    }
}

class PClass(
    override val parent: TagHandler,
    val vertexIndex: Int,
    val normalIndex: Int,
    val textureIndex: Int,
    val colorIndex: Int
) : TagHandler {

    var vertexIndices = emptyList<Int>()
    var normalIndices = emptyList<Int>()
    var textureIndices = emptyList<Int>()
    var colorIndices = emptyList<Int>()

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        else -> NopTagHandler(this)
    }

    override fun consume(text: String) {
        val chunk = max(vertexIndex, max(normalIndex, max(textureIndex, colorIndex)))
        text.split(" ").chunked(chunk + 1).forEach {
            if (vertexIndex >= 0) vertexIndices += it[vertexIndex].toInt()
            if (normalIndex >= 0) normalIndices += it[normalIndex].toInt()
            if (textureIndex >= 0) textureIndices += it[textureIndex].toInt()
            if (colorIndex >= 0) colorIndices += it[colorIndex].toInt()
        }
    }

    override fun endParsing(): TagHandler {
        parserListener.pushVertexIndices(vertexIndices)
        parserListener.pushColorIndices(colorIndices)
        parserListener.pushNormalIndices(normalIndices)
        return super.endParsing()
    }
}

class FloatArrayTag(override val parent: TagHandler) : TagHandler {

    var floatArray = floatArrayOf()

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler {
        return NopTagHandler(this)
    }

    override fun consume(text: String) {
        floatArray = text.split(" ").map { it.toFloat() }.toFloatArray()
    }
}

class TechniqueCommon(override val parent: TagHandler) : TagHandler {

    lateinit var factory: NumberFactory

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "accessor" -> Accessor(this).also {
            val stride = attributes["stride"]?.toString()?.toInt()
            factory = when (stride) {
                2 -> Vector2Factory
                3 -> Vector3Factory
                4 -> Vector4Factory
                else -> throw IllegalArgumentException("stride of '$stride' not supported.")
            }
        }
        else -> NopTagHandler(this)
    }
}

class Accessor(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler {
        return NopTagHandler(this)
    }
}

interface NumberFactory {
    fun build(array: FloatArray): List<Vector>
}

interface Vector

data class Vector2(val x: Number, val y: Number) : Vector
data class Vector3(val x: Number, val y: Number, val z: Number) : Vector
data class Vector4(val x: Number, val y: Number, val z: Number, val w: Number) : Vector

object Vector2Factory : NumberFactory {
    override fun build(array: FloatArray): List<Vector> {
        return array.toList().chunked(2).map {
            val (x, y) = it
            Vector2(x, y)
        }
    }
}

object Vector3Factory : NumberFactory {
    override fun build(array: FloatArray): List<Vector> {
        return array.toList().chunked(3).map {
            val (x, y, z) = it
            Vector3(x, y, z)
        }
    }
}

object Vector4Factory : NumberFactory {
    override fun build(array: FloatArray): List<Vector> {
        return array.toList().chunked(4).map {
            val (x, y, z, w) = it
            Vector4(x, y, z, w)
        }
    }
}
