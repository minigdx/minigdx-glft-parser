package collada.tags

class NopTagHandler(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler {
        return NopTagHandler(this)
    }

    override fun endParsing(): TagHandler {
        return parent
    }
}
