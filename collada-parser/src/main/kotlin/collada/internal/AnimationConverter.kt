package collada.internal

import collada.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class AnimationConverter : InternalConverter<AnimationsDescription> {

    private fun toBoneData(element: Element): List<KeyFrame> {
        val (keyframes, transforms, interpolations) = element.getElementsByTag("source")
        val times = keyframes.getElementsByTag("float_array")
            .first()
            .text()
            .split(" ")
            .map { it.toFloat() }

        val matrix = transforms.getElementsByTag("float_array")
            .first()
            .text()
            .split(" ")
            .map { it.toFloat() }
            .chunked(16)

        val interpolationsName = interpolations.getElementsByTag("Name_array")
            .first()
            .text()
            .split(" ")

        val frames = times.zip(matrix) { a, b -> a to b }.zip(interpolationsName) { a, b ->
            KeyFrame(
                time = a.first,
                transformation = Transformation(a.second.toFloatArray()),
                interpolation = b
            )
        }

        return frames
    }
    private fun toAnimation(element: Element): Animation {
        val name = element.attr("id")
        val frames = element.children().flatMap { toBoneData(element) }
        val boneId = element.getElementsByTag("channel")
            .first()
            .attr("target")
            .split("/")
            .first()
        return Animation(
            name = name,
            boneId = boneId,
            keyFrames = frames
        )
    }

    override fun convert(document: Document): AnimationsDescription {
        val root = document.getElementsByTag("library_animations")
            .firstOrNull() ?: return EmptyAnimations

        return Animations(animations = root.children().map { toAnimation(it) })
    }
}
