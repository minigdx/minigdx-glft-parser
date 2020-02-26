import collada.Converter
import collada.Mesh
import collada.Model
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class ConverterTest {

    @TempDir
    lateinit var dir: File

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "{0}.dae ➡️ {0}.3d")
    @ValueSource(
        strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature"]
    )
    fun parse(name: String) {
        val protobuf = dir.resolve("$name.bin")
        Converter(File("src/test/resources/$name.dae")).toProtobuf(protobuf)
        collada.Model.readProtobuf(protobuf.readBytes())

        val json = dir.resolve("$name.json")
        Converter(File("src/test/resources/$name.dae")).toJson(json)
        collada.Model.readJson(json.readBytes())
    }
}
