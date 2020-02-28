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

    val colladaApiTestDirectory=  File("../collada-api/src/commonTest/resources")

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "{0}.dae ➡️ {0}.json and {0}.protobuf")
    @ValueSource(
        strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature"]
    )
    fun parse(name: String) {
        val protobuf = dir.resolve("$name.protobuf")
        Converter(File("src/test/resources/$name.dae")).toProtobuf(protobuf)
        collada.Model.readProtobuf(protobuf.readBytes())

        val json = dir.resolve("$name.json")
        Converter(File("src/test/resources/$name.dae")).toJson(json)
        collada.Model.readJson(json.readBytes())
    }

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "Update collada-api resources {0}")
    @ValueSource(
        strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature"]
    )
    fun updateColladaApiTestResources(name: String) {
        val protobuf = colladaApiTestDirectory.resolve("$name.protobuf")
        Converter(File("src/test/resources/$name.dae")).toProtobuf(protobuf)

        val json = colladaApiTestDirectory.resolve("$name.json")
        Converter(File("src/test/resources/$name.dae")).toJson(json)
    }
}
