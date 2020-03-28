package collada.internal

import collada.*
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class AnimationConverter : InternalConverter<AnimationsDescription> {

    class KeyFrameData(
        val time: Float,
        val transformations: MutableMap<String, Transformation> = mutableMapOf(),
        val interpolation: String
    ) {
        fun toKeyFrame(): KeyFrame {
            return KeyFrame(
                time = time,
                transformations = transformations.toMap(),
                interpolation = interpolation
            )
        }
    }

    private fun toBoneData(
        element: Element,
        keyFrames: MutableMap<Float, KeyFrameData>
    ) {
        val boneId = element.getElementsByTag("channel")
            .first()
            .attr("target")
            .split("/")
            .first()

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

        times.zip(matrix) { a, b -> a to b }.zip(interpolationsName) { a, b ->
            val currentKeyFrame = keyFrames.getOrPut(a.first) {
                KeyFrameData(
                    time = a.first,
                    interpolation = b
                )
            }
            currentKeyFrame.transformations[boneId] = Transformation(a.second.toFloatArray())
        }
    }

    private fun toAnimation(element: Element): Animation {

        val name = element.attr("id")
        val keyFrames = mutableMapOf<Float, KeyFrameData>()
        element.children().forEach { toBoneData(it, keyFrames) }
        return Animation(
            name = name,
            keyFrames = keyFrames.values.map { it.toKeyFrame() }
        )
    }

    override fun convert(document: Document): AnimationsDescription {
        val root = document.getElementsByTag("library_animations")
            .firstOrNull { it.getElementsByTag("channel").isNotEmpty() } ?: return EmptyAnimations

        return Animations(animations = root.children().map { toAnimation(it) })
    }
}
