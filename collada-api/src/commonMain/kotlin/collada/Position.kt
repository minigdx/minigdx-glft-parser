package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Position(
    @SerialId(1)
    val x: Float,
    @SerialId(2)
    val y: Float,
    @SerialId(3)
    val z: Float
)
