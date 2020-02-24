package collada.listener

interface ParserListener {

    fun startMesh(name: String)

    fun pushPositions(positions: List<Vector3>)

    fun pushNormals(normals: List<Vector3>)

    fun pushColors(colors: List<Vector4>)

    fun pushVertexIndices(indices: List<Int>)

    fun pushColorIndices(indices: List<Int>)

    fun pushNormalIndices(indices: List<Int>)

    fun endMesh()

    fun pushArmature(root: Bone)
}
