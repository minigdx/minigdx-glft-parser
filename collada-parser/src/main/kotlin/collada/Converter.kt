package collada

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.io.File

class Converter(private val input: File) {

    private fun <T> Element.chunkValue(chunkSize: Int, factory: (List<Float>) -> T): List<T> {
        val floatArray = this.getElementsByTag("float_array").first()
        return floatArray.text()
            .split(" ")
            .map { it.toFloat() }
            .chunked(chunkSize)
            .map { factory.invoke(it) }
    }

    private fun Element.convertToPositions(): List<Position> {
        return this.chunkValue(3) {
            val (x, y, z) = it
            Position(x, y, z)
        }
    }

    private fun Element.convertToNormals(): List<Normal> {
        return this.chunkValue(3) {
            val (x, y, z) = it
            Normal(x, y, z)
        }
    }

    private fun Element.convertToColors(): List<Color> {
        return this.chunkValue(4) {
            val (r, g, b, a) = it
            Color(r, g, b, a)
        }
    }

    data class Triangle(val vertexIndex: Int, val normalIndex: Int, val colorIndex: Int)

    private fun Element.convertToTriangles(): List<Triangle> {
        val stride = this.getElementsByTag("input").count()
        val converter: (List<Int>) -> Triangle = when (stride) {
            1 -> { lst: List<Int> -> Triangle(lst[0], 0, 0) }
            2 -> { lst: List<Int> -> Triangle(lst[0], lst[1], 0) }
            3 -> { lst: List<Int> -> Triangle(lst[0], lst[1], 0) }
            4 -> { lst: List<Int> -> Triangle(lst[0], lst[1], lst[3]) }
            else -> throw IllegalArgumentException("Convertion of '$stride' arguments not supported.")
        }
        return this.getElementsByTag("p")
            .first()
            .text()
            .split(" ")
            .map { it.toInt() }
            .chunked(stride, converter)
    }

    private fun createMesh(geometry: Element): Mesh {
        val sources = geometry.getElementsByTag("source")

        val positions = sources.first { it.attr("id").endsWith("positions") }
            .convertToPositions()

        val normals = sources.first { it.attr("id").endsWith("normals") }
            .convertToNormals()
            .ifEmpty { listOf(Normal(1f, 1f, 1f)) }

        val colors = sources.firstOrNull { it.attr("id").contains("colors") }
            ?.convertToColors() ?: listOf(Color(0f, 0f, 0f, 1f))

        val triangles = geometry.getElementsByTag("triangles")
            .first()
            .convertToTriangles()

        val indexes = triangles.toSet()
            .mapIndexed { index, triangle -> triangle to index }
            .toMap()

        val verticesOrder = triangles.map {
            indexes[it]!!
        }.toIntArray()

        val vertices = indexes.map {
            Vertex(
                position = positions[it.key.vertexIndex],
                color = colors[it.key.colorIndex],
                normal = normals[it.key.normalIndex]
            )
        }
        return Mesh(vertices = vertices, verticesOrder = verticesOrder)
    }

    private fun createArmature(element: Element): Armature {

        fun _createArmature(parent: Bone? = null, bone: Element): Bone {
            val transform = bone.getElementsByTag("matrix").first()
            val transformation = Transformation(transform.text().split(" ").map { it.toFloat() }.toFloatArray())
            val boneObj = Bone(
                id = bone.attr("sid"),
                parent = parent,
                transformation = transformation,
                childs = emptyList(),
                weights = emptyList()
            )
            boneObj.childs = bone.children()
                .filter { it.attr("type") == "NODE" }
                .map { _createArmature(boneObj, it) }

            return boneObj
        }
        val root = element.getElementsByAttributeValue("type", "NODE")
            .first()
        return Armature(_createArmature(null, root))
    }

    @ImplicitReflectionSerializer
    fun toProtobuf(output: File) {
        val document = Jsoup.parse(input.readText(), "", Parser.xmlParser())
        val mesh = document.getElementsByTag("library_geometries")
            .first()
            .getElementsByTag("geometry")
            .first()
            .let { createMesh(it) }

        val armature = document.getElementsByTag("library_visual_scenes")
            .first()
            .getElementsByTag("node")
            .firstOrNull { it.attr("id") == "Armature" && it.attr("type") == "NODE" }
            ?.let { createArmature(it) }

        val data = ProtoBuf.dump(Model.serializer(), Model(
            mesh = mesh,
            armature = armature
        ))
        output.writeBytes(data)
    }
}
