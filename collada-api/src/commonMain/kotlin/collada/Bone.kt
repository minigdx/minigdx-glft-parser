package collada

import kotlinx.serialization.Serializable

@Serializable
class Weight(
    val vertex: Vertex,
    val weight: Float
)

@Serializable
class Bone(
    val id: String,
    var childs: List<Bone> = emptyList(),
    val transformation: Transformation,
    val weights: List<Weight> = emptyList()
)
