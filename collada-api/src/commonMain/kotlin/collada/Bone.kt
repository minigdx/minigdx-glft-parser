package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class Weight(
    @ProtoId(1)
    val vertex: Vertex,
    @ProtoId(2)
    val weight: Float
)

@Serializable
class Bone(
    @ProtoId(1)
    val id: String,
    @ProtoId(2)
    var childs: List<Bone> = emptyList(),
    @ProtoId(3)
    val transformation: Transformation,
    @ProtoId(4)
    val weights: List<Weight> = emptyList(),
    @ProtoId(5)
    val inverseBindPose: Transformation
)
