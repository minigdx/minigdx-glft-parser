package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Transformation(
    @ProtoId(1)
    val matrix: FloatArray
)
