package collada

import kotlin.test.Ignore
import kotlin.test.Test

interface Content<T> {

    fun onLoad(callback: (T) -> Unit)
}

class NoContent<T> : Content<T> {
    // Nop
    override fun onLoad(callback: (T) -> Unit) = Unit
}

expect class FileHandler {

    fun readBytes(name: String): Content<ByteArray>

    companion object {
        fun build(): FileHandler
    }
}

@ExperimentalStdlibApi
class ReadWriteTest {

    private val fileHandler = FileHandler.build()

    private fun readWriteProtobuf(it: ByteArray) {
        val result = Model.readProtobuf(it)
        Model.writeProtobuf(result)
    }


    private fun readWriteJson(it: ByteArray) {
        val result = Model.readJson(it)
        Model.writeJson(result)
    }

    @Test
    fun cubeProtobufReadAndWrite() {
        fileHandler.readBytes("cube.protobuf").onLoad {
            readWriteProtobuf(it)
        }
    }

    @Test
    fun cubeJsonReadAndWrite() {
        fileHandler.readBytes("cube.json").onLoad {
            readWriteJson(it)
        }
    }

    @Test
    fun monkey2ColorProtobufReadAndWrite() {
        fileHandler.readBytes("monkey_color2.protobuf").onLoad {
            readWriteProtobuf(it)
        }
    }

    @Test
    fun monkey2ColorJsonReadAndWrite() {
        fileHandler.readBytes("monkey_color2.json").onLoad {
            readWriteJson(it)
        }
    }


    @Test
    fun armatureProtobufReadAndWrite() {
        fileHandler.readBytes("armature.protobuf").onLoad {
            readWriteProtobuf(it)
        }
    }

    @Test
    fun armatureJsonReadAndWrite() {
        fileHandler.readBytes("armature.json").onLoad {
            readWriteJson(it)
        }
    }
}
