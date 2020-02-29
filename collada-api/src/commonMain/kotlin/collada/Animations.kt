package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

interface AnimationsDescription

@Serializable
object EmptyAnimations : AnimationsDescription

@Serializable
class Animations(
    @SerialId(1)
    val animations: List<Animation> = emptyList()
) : AnimationsDescription


@Serializable
class Animation(
    @SerialId(1)
    val name: String,
    @SerialId(2)
    val boneId: String,
    @SerialId(3)
    val keyFrames: List<KeyFrame>
)

@Serializable
class KeyFrame(
    @SerialId(1)
    val time: Float,
    @SerialId(2)
    val transformation: Transformation,
    @SerialId(3)
    val interpolation: String // not used now
)
