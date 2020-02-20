package collada.tags

import collada.ParserListener

interface TagHandler {

    val parent: TagHandler

    val parserListener: ParserListener
    get() {
        return parent.parserListener
    }

    fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler

    fun consume(text: String) = Unit

    fun endParsing(): TagHandler {
        return parent
    }
}
