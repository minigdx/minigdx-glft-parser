package collada

import kotlinx.serialization.Serializable

@Serializable
class Vertex(
    val position: Position,
    val color: Color,
    val normal: Normal
)
