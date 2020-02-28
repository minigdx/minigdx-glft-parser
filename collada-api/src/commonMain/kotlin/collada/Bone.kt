package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

@Serializable
class Weight(
    @SerialId(1)
    val vertex: Vertex,
    @SerialId(2)
    val weight: Float
)

@Serializable
class Bone(
    @SerialId(1)
    val id: String,
    @SerialId(2)
    var childs: List<Bone> = emptyList(),
    @SerialId(3)
    val transformation: Transformation,
    @SerialId(4)
    val weights: List<Weight> = emptyList()
)
