package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

interface SkinDescription

@Serializable
object EmptySkin : SkinDescription

@Serializable
class WeightInfluence(
    @ProtoId(1)
    val boneId: String,
    @ProtoId(2)
    val weight: Float
)

@Serializable
class Influence(@ProtoId(1) val weights: List<WeightInfluence> = emptyList())

@Serializable
class Skin(
    @ProtoId(1)
    val influences: List<Influence> = emptyList()
) : SkinDescription
