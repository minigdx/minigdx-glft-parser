package collada.tags

import collada.listener.Bone

class LibraryVisualScenes(override val parent: TagHandler) : TagHandler {

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "visual_scene" -> VisualScene(this)
        else -> NopTagHandler(this)
    }
}

class VisualScene(override val parent: TagHandler) : TagHandler {

    var root: List<Node> = emptyList()

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "node" -> Node(
            this,
            attributes["type"].toString(),
            attributes["name"].toString()
        ).also {
            if (attributes["id"] == "Armature") root += it
        }
        else -> NopTagHandler(this)
    }

    override fun endParsing(): TagHandler {
        root.forEach {
            parserListener.pushArmature(it.build())
        }
        return super.endParsing()
    }
}

class Node(override val parent: TagHandler, val type: String, val sid: String) : TagHandler {

    lateinit var matrix: Matrix
    var nodes = emptyList<Node>()

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = when (tagName) {
        "node" -> Node(this, attributes["type"].toString(), attributes["sid"].toString()).also { nodes += it }
        "matrix" -> Matrix(this).also { matrix = it }
        else -> NopTagHandler(this)
    }
}

class Matrix(override val parent: TagHandler) : TagHandler {

    val floatArray = FloatArray(16) { 0f }

    override fun startParsing(tagName: String, attributes: Map<String, Any>): TagHandler = NopTagHandler(this)

    override fun consume(text: String) {
        text.split(" ").map { it.toFloat() }.forEachIndexed { index, fl ->
            floatArray[index] = fl
        }
    }
}

private val cache: MutableMap<String, Bone> = mutableMapOf()

private fun Node.build(): Bone {
    val p = if (this.parent is Node) {
        cache[this.parent.sid]
    } else {
        null
    }
    val bone = Bone(this.sid, p, emptyList(), this.matrix.floatArray)
    cache[this.sid] = bone
    bone.childs = this.nodes.filter { it.type == "JOINT" }.map { it.build() }
    return bone
}
