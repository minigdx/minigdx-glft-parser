import collada.GltfToMiniGdx
import collada.Model
import kotlinx.serialization.ImplicitReflectionSerializer
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class GltfToMiniGdxTest {

    @TempDir
    lateinit var dir: File

    @ExperimentalStdlibApi
    @ImplicitReflectionSerializer
    @ParameterizedTest(name = "{0}.gltf ➡️ {0}.json and {0}.protobuf")
    @ValueSource(strings = ["cube.gltf", "monkey_animation.gltf", "camera.gltf"])
    fun parse(name: String) {
        val protobuf = dir.resolve("$name.protobuf")
        GltfToMiniGdx(File("src/test/resources/$name")).toProtobuf(protobuf)

        val json = dir.resolve("$name.json")
        GltfToMiniGdx(File("src/test/resources/$name")).toJson(json)

        val model = Model.readProtobuf(protobuf.readBytes())
        require(model.mesh.vertices.isNotEmpty())
        Model.readJson(json.readBytes())
    }
}
