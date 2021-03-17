package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import com.curiouscreature.kotlin.math.Quaternion
import com.curiouscreature.kotlin.math.scale
import com.curiouscreature.kotlin.math.translation
import com.dwursteisen.minigdx.scene.api.common.Transformation

val GltfNode.transformation: Transformation
    get() {
        val t = translation.let { Float3(it.x, it.y, it.z) }
            .let { translation(it) }

        val r = rotation.let { Quaternion(it.i, it.j, it.k, it.a) }
            .let { Mat4.from(it) }

        val s = scale.let { Float3(it.x, it.y, it.z) }
            .let { scale(it) }

        return fromComposite(t, r, s)
    }

val GltfNode.isBox: Boolean
    get() {
        return this.mesh == null &&
            this.camera == null &&
            this.skin == null &&
            this.weights == null
    }
