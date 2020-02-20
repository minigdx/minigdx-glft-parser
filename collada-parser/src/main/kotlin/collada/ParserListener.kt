package collada

import collada.tags.Vector3
import collada.tags.Vector4

interface ParserListener {
    fun startMesh(name: String)

    fun pushPositions(positions: List<Vector3>)

    fun pushNormals(normals: List<Vector3>)

    fun pushColors(colors: List<Vector4>)

    fun pushVertexIndices(indices: List<Int>)

    fun pushColorIndices(indices: List<Int>)

    fun endMesh()
}
