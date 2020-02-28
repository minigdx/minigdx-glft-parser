package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Color (
    @SerialId(1)
    val r: Float,
    @SerialId(2)
    val g: Float,
    @SerialId(3)
    val b: Float,
    @SerialId(4)
    val a: Float
)
