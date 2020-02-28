package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Vertex(
    @SerialId(1)
    val position: Position,
    @SerialId(2)
    val color: Color,
    @SerialId(3)
    val normal: Normal
)
