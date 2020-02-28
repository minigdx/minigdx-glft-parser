package collada.internal

import collada.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ArmatureConverter : InternalConverter<ArmatureDescription> {

    private fun createArmature(element: Element): Armature {

        fun _createArmature(bone: Element): Bone {
            val transform = bone.getElementsByTag("matrix").first()
            val transformation = Transformation(transform.text().split(" ")
                .map { it.toFloat() }
                .toFloatArray())

            val childs = bone.children()
                .filter { it.attr("type") == "JOINT" }
                .map { _createArmature(it) }

            val boneObj = Bone(
                id = bone.attr("sid"),
                transformation = transformation,
                childs = childs,
                weights = emptyList()
            )

            return boneObj
        }

        val root = element.getElementsByAttributeValue("type", "NODE")
            .first()
        return Armature(_createArmature(root))
    }

    override fun convert(document: Document): ArmatureDescription {
        return document.getElementsByTag("library_visual_scenes")
            .first()
            .getElementsByTag("node")
            .firstOrNull { it.attr("id") == "Armature" && it.attr("type") == "NODE" }
            ?.let { createArmature(it) }
            ?: EmptyArmature
    }

}
