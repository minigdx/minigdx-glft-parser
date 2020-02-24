package collada.tags

import collada.listener.ParserListener

class RootTag(override val parserListener: ParserListener) : TagHandler {

    override val parent: TagHandler = this

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when(tagName) {
        "COLLADA" -> Collada(this)
        else -> throw IllegalArgumentException("Expected a Collada XML file. It starts with '<COLLADA>' tag, got '<$tagName>' instead.")
    }

    override fun endParsing(): TagHandler {
        return this
    }
}
