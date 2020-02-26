package collada

import kotlinx.serialization.Serializable

@Serializable
class Model(
    val mesh: Mesh,
    val armature: Armature?
)
