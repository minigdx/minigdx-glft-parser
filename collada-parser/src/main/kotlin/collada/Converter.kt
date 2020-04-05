package collada

import java.io.File

interface Converter {

    val input: File

    fun toProtobuf(output: File)

    fun toJson(output: File)
}
