package collada

import kotlinx.serialization.Serializable

interface ArmatureDescription

@Serializable
object EmptyArmature : ArmatureDescription

@Serializable
class Armature(
    val rootBone: Bone
) : ArmatureDescription
