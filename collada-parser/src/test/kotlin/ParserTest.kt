import collada.Parser
import mini.gdx.MiniGdxFile
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class ParserTest {

    @TempDir
    lateinit var dir: File

    @ParameterizedTest(name = "{0}.dae ➡️ {0}.3d")
    @ValueSource(
        strings = ["cube", "monkey", "sample", "cube_color", "monkey_color", "monkey_color2", "armature"]
    )
    fun parse(name: String) {
        val outputFile = dir.resolve("$name.3d")
        Parser(MiniGdxFile(outputFile)).parse("src/test/resources/$name.dae")
        println(outputFile.readText())
    }
}
