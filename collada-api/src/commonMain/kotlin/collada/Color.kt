package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Color (
    @ProtoId(1)
    val r: Float,
    @ProtoId(2)
    val g: Float,
    @ProtoId(3)
    val b: Float,
    @ProtoId(4)
    val a: Float
)
