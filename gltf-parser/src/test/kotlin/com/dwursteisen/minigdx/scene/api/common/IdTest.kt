package com.dwursteisen.minigdx.scene.api.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class IdTest {

    @Test
    fun generate_new_random_id() {
        val id = Id()
        assertNotEquals(id.value, Id().value)
        assertEquals(8, id.value.length)
    }
}
