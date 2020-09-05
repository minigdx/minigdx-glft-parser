package com.dwursteisen.minigdx.scene.api.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoId
import kotlin.random.Random

typealias Id = Int

@Serializable
data class NewId(
    @ProtoId(0)
    val value: String = generate()
) {

    companion object {
        private fun generate(): String {
            val randomValues = ByteArray(ID_SIZE)
            Random.nextBytes(randomValues)
            return randomValues.map { it.toInt() and 0x0F }
                .joinToString("") { CONVERT[it] }

        }

        private const val ID_SIZE = 8

        private val CONVERT = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
    }
}
