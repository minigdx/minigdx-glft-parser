package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

@Serializable
class InfluenceData(
    @ProtoId(1)
    val boneId: String,
    @ProtoId(2)
    val weight: Float
)

@Serializable
class Influence(
    @ProtoId(1) val data: List<InfluenceData> = emptyList()
)

@Serializable
class Vertex(
    @ProtoId(1)
    val position: Position,
    @ProtoId(2)
    val color: Color,
    @ProtoId(3)
    val normal: Normal,
    @ProtoId(4)
    val influence: Influence
)
