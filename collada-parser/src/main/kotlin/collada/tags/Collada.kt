package collada.tags

class Collada(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when(tagName) {
        "library_geometries" -> LibraryGeometries(this)
        else -> NopTagHandler(this)
    }
}
