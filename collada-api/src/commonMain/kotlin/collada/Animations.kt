package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

interface AnimationsDescription

@Serializable
object EmptyAnimations : AnimationsDescription

@Serializable
class Animations(
    @ProtoId(1)
    val animations: List<Animation> = emptyList()
) : AnimationsDescription


@Serializable
class Animation(
    @ProtoId(1)
    val name: String,
    @ProtoId(2)
    val boneId: String,
    @ProtoId(3)
    val keyFrames: List<KeyFrame>
)

@Serializable
class KeyFrame(
    @ProtoId(1)
    val time: Float,
    @ProtoId(2)
    val transformation: Transformation,
    @ProtoId(3)
    val interpolation: String // not used now
)
