package collada

import java.io.File

class SyncContent<T>(private val content: T) : Content<T> {

    override fun onLoad(callback: (T) -> Unit) {
        callback(content)
    }
}

actual class FileHandler {

    actual fun readBytes(name: String): Content<ByteArray> {
        return SyncContent(File(name).readBytes())
    }

    actual companion object {
        actual fun build(): FileHandler {
            return FileHandler()
        }
    }
}
