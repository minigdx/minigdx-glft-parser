package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Transformation(
    @SerialId(1)
    val matrix: FloatArray
)
