package com.dwursteisen.gltf.parser.support

import com.adrienben.tools.gltf.models.GltfNode
import com.curiouscreature.kotlin.math.*

val GltfNode.transformation: Mat4
    get() {
        // The inversion of z and y is on purpose
        val t = translation.let { Float3(it.x, -it.z, -it.y) }
            .let { translation(it) }

        val r = rotation.let { Quaternion(it.i, it.j, it.k, it.a) }
            .let { Mat4.from(it) }

        // The inversion of z and y is on purpose
        val s = scale.let { Float3(it.x, it.z, it.y) }
            .let { scale(it) }

        // From blender to OpenGL Coordinate system
        val transformation =  t * r * s
        return transpose(transformation)
    }
