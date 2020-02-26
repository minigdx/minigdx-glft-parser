package collada.internal

import collada.EmptySkin
import collada.SkinDescription
import collada.Skin
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.math.round

class SkinConverter : InternalConverter<SkinDescription> {

    private fun createSkin(element: Element): Skin {
        val boneIds = element.getElementsByTag("Name_array")
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
                Weight(boneIds[id], weights[w])
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
            val sumByDouble = it.weights.sumByDouble { it.weight.toDouble() }
            require(it.weights.isEmpty() || round(sumByDouble * 100).toInt() == 100) { "Weight on vertex should be equal to 1.0 (was '$sumByDouble')"}
        }
        return Skin()
    }

    class Weight(val boneId: String, val weight: Float)
    class Influence(val weights: List<Weight>)

    override fun convert(document: Document): SkinDescription {
        return document.getElementsByTag("library_controllers")
            .firstOrNull()
            ?.getElementsByTag("skin")
            ?.firstOrNull()
            ?.let { createSkin(it) }
            ?: EmptySkin
    }

}