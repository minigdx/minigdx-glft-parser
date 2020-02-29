package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

interface SkinDescription

@Serializable
object EmptySkin : SkinDescription

@Serializable
class WeightInfluence(
    @SerialId(1)
    val boneId: String,
    @SerialId(2)
    val weight: Float
)

@Serializable
class Influence(@SerialId(1) val weights: List<WeightInfluence> = emptyList())

@Serializable
class Skin(
    @SerialId(1)
    val influences: List<Influence> = emptyList()
) : SkinDescription
