package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Vertex(
    @ProtoId(1)
    val position: Position,
    @ProtoId(2)
    val color: Color,
    @ProtoId(3)
    val normal: Normal
)
