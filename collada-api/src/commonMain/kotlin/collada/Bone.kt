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
    val parent: Bone?,
    var childs: List<Bone>,
    val transformation: Transformation,
    val weights: List<Weight>
)
