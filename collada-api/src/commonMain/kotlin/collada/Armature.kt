package collada

import kotlinx.serialization.SerialId
import kotlinx.serialization.Serializable

interface ArmatureDescription

@Serializable
object EmptyArmature : ArmatureDescription

@Serializable
class Armature(
    @SerialId(1)
    val rootBone: Bone
) : ArmatureDescription
