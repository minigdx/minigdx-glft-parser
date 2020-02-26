package collada

import kotlinx.serialization.Serializable

@Serializable
class Armature(
    val rootBone: Bone
)
