package collada

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId

enum class CameraType {
    PERSPECTIVE,
    ORTHOGRAPHIC
}

@Serializable
data class CameraParameters(
    @ProtoId(0)
    val zFar: Float,
    @ProtoId(1)
    val zNear: Float,
    @ProtoId(2)
    val perspectiveFov: Float,
    @ProtoId(3)
    val orthographicScale: Float
)

@Serializable
data class Camera(
    @ProtoId(0)
    val name: String,
    @ProtoId(1)
    val type: CameraType,
    @ProtoId(2)
    val parameters: CameraParameters,
    @ProtoId(3)
    val transformation: Transformation
)
