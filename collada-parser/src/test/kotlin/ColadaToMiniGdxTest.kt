import collada.ColadaToMiniGdx
import collada.Model
import kotlinx.serialization.ImplicitReflectionSerializer
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class ColadaToMiniGdxTest {

    @TempDir
    lateinit var dir: File

    val colladaApiTestDirectory=  File("../collada-api/src/commonTest/resources")

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "{0}.dae ➡️ {0}.json and {0}.protobuf")
    @ValueSource(strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature", "bones"])
    fun parse(name: String) {
        val protobuf = dir.resolve("$name.protobuf")
        ColadaToMiniGdx(File("src/test/resources/$name.dae")).toProtobuf(protobuf)
        Model.readProtobuf(protobuf.readBytes())

        val json = dir.resolve("$name.json")
        ColadaToMiniGdx(File("src/test/resources/$name.dae")).toJson(json)
        Model.readJson(json.readBytes())
    }

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "Update collada-api resources {0}")
    @ValueSource(
        strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature"]
    )
    fun updateColladaApiTestResources(name: String) {
        val protobuf = colladaApiTestDirectory.resolve("$name.protobuf")
        ColadaToMiniGdx(File("src/test/resources/$name.dae")).toProtobuf(protobuf)

        val json = colladaApiTestDirectory.resolve("$name.json")
        ColadaToMiniGdx(File("src/test/resources/$name.dae")).toJson(json)
    }
}
