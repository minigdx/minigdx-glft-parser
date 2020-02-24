package collada.listener

interface Vector

data class Vector2(val x: Number, val y: Number) : Vector
data class Vector3(val x: Number, val y: Number, val z: Number) : Vector
data class Vector4(val x: Number, val y: Number, val z: Number, val w: Number) : Vector
