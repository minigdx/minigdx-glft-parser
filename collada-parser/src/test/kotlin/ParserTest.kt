import collada.Parser
import mini.gdx.MiniGdxFile
import java.io.File

fun main() {
    Parser(MiniGdxFile(File("cube.3d"))).parse("collada-parser/src/test/resources/cube.dae")
    Parser(MiniGdxFile(File("monkey.3d"))).parse("collada-parser/src/test/resources/monkey.dae")
    Parser(MiniGdxFile(File("sample.3d"))).parse("collada-parser/src/test/resources/sample.dae")
    Parser(MiniGdxFile(File("monkey_color.3d"))).parse("collada-parser/src/test/resources/monkey_color.dae")
    Parser(MiniGdxFile(File("cube_color.3d"))).parse("collada-parser/src/test/resources/cube_color.dae")
    Parser(MiniGdxFile(File("monkey_color2.3d"))).parse("collada-parser/src/test/resources/monkey_color2.dae")
}
