package collada

actual class FileHandler {

    actual fun readBytes(name: String): Content<ByteArray> = NoContent()

    actual companion object {
        actual fun build(): FileHandler {
            return FileHandler()
        }
    }
}
