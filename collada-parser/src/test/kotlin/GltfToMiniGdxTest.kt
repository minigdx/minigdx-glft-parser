import collada.Armature
import collada.ColladaToMiniGdx
import collada.GltfToMiniGdx
import collada.Model
import com.curiouscreature.kotlin.math.Mat4
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
    @ValueSource(strings = ["cube.gltf", "monkey_animation.gltf"])
    fun parse(name: String) {
        /*
        val protobuf = dir.resolve("$name.protobuf")
        GltfToMiniGdx(File("src/test/resources/$name")).toProtobuf(protobuf)

        val json = dir.resolve("$name.json")
        GltfToMiniGdx(File("src/test/resources/$name")).toJson(json)

        val model = Model.readProtobuf(protobuf.readBytes())
        require(model.mesh.vertices.isNotEmpty())
        Model.readJson(json.readBytes())
*/
        // ---- to remove

        val fromGltf = dir.resolve("bones_gltf.protobuf")
        GltfToMiniGdx(File("src/test/resources/basic_armature.gltf")).toProtobuf(fromGltf)
        val modelGltf = Model.readProtobuf(fromGltf.readBytes())

        // collada
        val fromCollada = dir.resolve("bones_collada.protobuf")
        ColladaToMiniGdx(File("src/test/resources/basic_armature.dae")).toProtobuf(fromCollada)
        val modelCollada = Model.readProtobuf(fromCollada.readBytes())

        val root = Mat4.of(*(modelGltf.armature as Armature).rootBone.transformation.matrix)
        val child = Mat4.of(*(modelGltf.armature as Armature).rootBone.childs.first().transformation.matrix)

        println((modelGltf.armature as Armature).rootBone.id)
        println("root  translation :"+root.translation)
        println("root  rotation    :"+root.rotation)
        println("root  scale       :"+root.scale)

        println((modelGltf.armature as Armature).rootBone.childs.first().id)
        println("child translation : " + child.translation)
        println("child rotation    : " + child.rotation)
        println("child scale       : " + child.scale)

        println("root + child posit:" + (root * child).position)

        val bb = Mat4.of(*(modelCollada.armature as Armature).rootBone.transformation.matrix)

        val cc = Mat4.of(*(modelCollada.armature as Armature).rootBone.childs.first().transformation.matrix)
        println(modelGltf)
        println(modelCollada)

        // TODO: comparer les matrix. Elles sembles proches.
        // TODO: utiliser Mat4 et faire transpose / inverse ?
    }
}
