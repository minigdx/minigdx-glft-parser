package com.dwursteisen.gltf.parser.support

import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.rotation
import com.curiouscreature.kotlin.math.scale
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.minigdx.scene.api.common.Transformation
import com.curiouscreature.kotlin.math.inverse as inv


fun fromComposite(translation: Mat4, rotation: Mat4, scale: Mat4): Transformation {
        return Transformation(
            translation.asGLArray().toFloatArray(),
            rotation.asGLArray().toFloatArray(),
            scale.asGLArray().toFloatArray()
        )
}

fun fromTransformation(transformation: Mat4): Transformation {
    return fromComposite(
        translation(transformation),
        rotation(transformation),
        scale(transformation)
    )
}

fun Transformation.inverse(): Transformation {
    return fromComposite(
        inv(Mat4.fromColumnMajor(*this.translation)),
        inv(Mat4.fromColumnMajor(*this.rotation)),
        inv(Mat4.fromColumnMajor(*this.scale)),
    )
}

val Transformation.combined: Mat4
get() {
    return Mat4.fromColumnMajor(*this.translation) *
            Mat4.fromColumnMajor(*this.rotation) *
            Mat4.fromColumnMajor(*this.scale)
}

fun FloatArray.asMat4(): Mat4 {
    return Mat4.fromColumnMajor(*this)
}
