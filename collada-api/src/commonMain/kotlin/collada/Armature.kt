package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

interface ArmatureDescription

@Serializable
object EmptyArmature : ArmatureDescription

@Serializable
class Armature(
    @ProtoId(1)
    val rootBone: Bone
) : ArmatureDescription
