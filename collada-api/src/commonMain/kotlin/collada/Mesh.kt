package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Mesh(
    @ProtoId(1)
    val vertices: List<Vertex> = emptyList(),
    @ProtoId(2)
    val verticesOrder: IntArray = intArrayOf()
)
