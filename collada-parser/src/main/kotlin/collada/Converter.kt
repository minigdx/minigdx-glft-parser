package collada

import collada.internal.AnimationConverter
import collada.internal.ArmatureConverter
import collada.internal.MeshConverter
import collada.internal.SkinConverter
import kotlinx.serialization.ImplicitReflectionSerializer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.File

class Converter(private val input: File) {

    @ImplicitReflectionSerializer
    fun toProtobuf(output: File) {
        val model = convertToModel()
        val data = Model.writeProtobuf(model)
        output.writeBytes(data)
    }

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    fun toJson(output: File) {
        val model = convertToModel()
        val data = Model.writeJson(model)
        output.writeBytes(data)
    }

    private val armatureConverter = ArmatureConverter()
    private val skinConverter = SkinConverter()
    private val animationsConverter = AnimationConverter()

    private fun convertToModel(): Model {
        val document: Document = Jsoup.parse(input.readText(), "", Parser.xmlParser())
        val skin = skinConverter.convert(document)
        val mesh = MeshConverter(skin).convert(document)
        val armature = armatureConverter.convert(document)
        val animations = animationsConverter.convert(document)

        val model = Model(
            mesh = mesh,
            armature = armature,
            animations = animations
        )
        return model
    }
}
