package collada

class SyncContent<T>(val content: T) : Content<T> {

    override fun onLoad(callback: (T) -> Unit) {
        callback(content)
    }
}


@JsModule("fs")
external class fs {

    companion object {

        fun readFileSync(name: String, encoding: String = definedExternally): ByteArray
    }
}

external object process {
    fun cwd(): String
}

actual class FileHandler {

    @ExperimentalStdlibApi
    actual fun readBytes(name: String): Content<ByteArray> {
        val filepath = process.cwd() + "/../../../../collada-api/src/commonTest/resources/" + name

        return SyncContent(fs.readFileSync(filepath))
    }

    actual companion object {
        actual fun build(): FileHandler {
            return FileHandler()
        }
    }

}
