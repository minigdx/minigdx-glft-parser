package collada.internal

import collada.Influence
import collada.InfluenceData
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.math.round

class Skin(val influences: List<Influence> = emptyList())

class SkinConverter : InternalConverter<Skin> {

    private fun createSkin(element: Element, boneSidToBoneId: Map<String, String>): Skin {
        val boneSids = element.getElementsByTag("Name_array")
            .first()
            .text()
            .split(" ")

        val weights = element.getElementsByTag("float_array")
            .first { it.attr("id").endsWith("weights-array") }
            .text()
            .split(" ")
            .map { it.toFloat() }
            .toFloatArray()

        val association = element.getElementsByTag("vertex_weights")
            .first()

        val allWeights = association.getElementsByTag("v")
            .first()
            .text()
            .split(" ")
            .map { it.toInt() }
            .chunked(2)
            .map {
                val (id, w) = it
                val sid = boneSids[id]
                InfluenceData(boneSidToBoneId.getValue(sid), weights[w])
            }

        val influence = association.getElementsByTag("vcount")
            .first()
            .text()
            .split(" ")
            .map { it.toInt() }

        var index = 0
        val influences = mutableListOf<Influence>()
        influence.forEach {
            val end = index + it
            val ww = allWeights.subList(index, end)
            index = end
            influences.add(Influence(ww))
        }
        influences.forEach {
            val sumByDouble = it.data.sumByDouble { it.weight.toDouble() }
            require(it.data.isEmpty() || round(sumByDouble * 100).toInt() == 100) { "Weight on vertex should be equal to 1.0 (was '$sumByDouble')" }
        }
        return Skin(
            influences = influences
        )
    }

    private fun createAssociation(document: Document): Map<String, String> {
        return document.getElementsByTag("node")
            .filter { it.attr("type") == "JOINT" }
            .map { it.attr("sid") to it.attr("id") }
            .toMap()
    }

    override fun convert(document: Document): Skin {
        val boneSidToBoneId = createAssociation(document)

        return document.getElementsByTag("library_controllers")
            .firstOrNull()
            ?.getElementsByTag("skin")
            ?.firstOrNull()
            ?.let { createSkin(it, boneSidToBoneId) }
            ?: Skin()
    }

}
