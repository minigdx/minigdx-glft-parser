package collada

class Weight(
    val vertex: Vertex,
    val weight: Float
)

class Bone(
    val id: String,
    val parent: Bone,
    val childs: List<Bone>,
    val transformation: Transformation,
    val weights: List<Weight>
)
