package collada

import kotlinx.serialization.Serializable

@Serializable
class Mesh(
    val vertices: List<Vertex>,
    val verticesOrder: IntArray
)
