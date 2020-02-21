package mini.gdx

import collada.ParserListener
import collada.tags.Vector3
import collada.tags.Vector4
import java.io.File

data class Mesh(
    val name: String,
    val positions: List<Vector3> = emptyList(),
    val normals: List<Vector3> = emptyList(),
    val colors: List<Vector4> = emptyList(),
    val vertexIndices: List<Int> = emptyList(),
    val colorIndices: List<Int> = emptyList(),
    val normalIndices: List<Int> = emptyList()
)

class MiniGdxFile(private val outputFile: File) : ParserListener {

    init {
        outputFile.writeText("MINIGDX V1\n")
    }
    var mesh: Mesh = Mesh("no_ready")

    override fun startMesh(name: String) {
        mesh = mesh.copy(name = name)
    }

    override fun pushPositions(positions: List<Vector3>) {
        mesh = mesh.copy(positions = positions)
    }

    override fun pushNormals(normals: List<Vector3>) {
        mesh = mesh.copy(normals = normals)
    }

    override fun pushColors(colors: List<Vector4>) {
        mesh = mesh.copy(colors = colors)
    }

    override fun pushVertexIndices(indices: List<Int>) {
        mesh = mesh.copy(vertexIndices = indices)
    }

    override fun pushColorIndices(indices: List<Int>) {
        mesh = mesh.copy(colorIndices = indices)
    }

    override fun pushNormalIndices(indices: List<Int>) {
        mesh = mesh.copy(normalIndices = indices)
    }

    override fun endMesh() {
        var meshStr = "MESH ${mesh.name}\n"
        meshStr += "POSITIONS ${mesh.positions.map { "${it.x} ${it.y} ${it.z}" }.joinToString(", ")}\n"
        meshStr += "NORMALS ${mesh.normals.map { "${it.x} ${it.y} ${it.z}" }.joinToString(", ")}\n"
        meshStr += "COLORS ${mesh.colors.map { "${it.x} ${it.y} ${it.z} ${it.w}" }.joinToString(", ")}\n"
        meshStr += "VERTEX_INDICES ${mesh.vertexIndices.joinToString(", ")}\n"
        meshStr += "COLOR_INDICES ${mesh.colorIndices.joinToString(", ")}\n"
        meshStr += "NORMAL_INDICES ${mesh.normalIndices.joinToString(", ")}\n"
        meshStr += "ENDMESH ${mesh.name}\n"
        outputFile.appendText(meshStr)
    }
}
