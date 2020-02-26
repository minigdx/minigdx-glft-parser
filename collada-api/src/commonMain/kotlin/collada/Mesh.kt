package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Mesh(
    @SerialId(1)
    val vertices: List<Vertex> = emptyList(),
    @SerialId(2)
    val verticesOrder: IntArray = intArrayOf()
)
