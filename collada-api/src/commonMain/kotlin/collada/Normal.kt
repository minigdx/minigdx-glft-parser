package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Normal(
    @ProtoId(1)
    val x: Float,
    @ProtoId(2)
    val y: Float,
    @ProtoId(3)
    val z: Float
)
