package collada.tags

import collada.ParserListener

class NopTagHandler(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler {
        level++
        val str = (0..level).map { "\t" }.joinToString("") + "--> $tagName"
        println(str)
        return NopTagHandler(this)
    }

    override fun endParsing(): TagHandler {
        val str = (0..level).map { "\t" }.joinToString("") + "<--"
        println(str)
        level--
        return parent
    }

    companion object {
        var level: Int = 0
    }
}
