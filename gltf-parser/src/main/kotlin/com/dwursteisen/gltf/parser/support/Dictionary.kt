package com.dwursteisen.gltf.parser.support

import com.dwursteisen.minigdx.scene.api.common.Id

class Dictionary {

    private val map: MutableMap<Any, Id> = mutableMapOf()

    fun get(any: Any): Id {
        return map.computeIfAbsent(any) { Id() }
    }
}
