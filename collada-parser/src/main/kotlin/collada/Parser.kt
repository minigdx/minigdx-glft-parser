package collada

import collada.tags.RootTag
import collada.tags.TagHandler
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

private fun Attributes?.toMap(): Map<String, Any> {
    return if (this == null) emptyMap()
    else {
        (0..this.length).map {
            val name = this.getLocalName(it)
            val value = this.getValue(it)
            name to value
        }.toMap()
    }
}

class Parser(val listener: ParserListener) {

    fun parse(file: String) {
        val parserFactory: SAXParserFactory = SAXParserFactory.newInstance()
        val parser: SAXParser = parserFactory.newSAXParser()
        val reader: XMLReader = parser.xmlReader

        var currentTagHandler: TagHandler = RootTag(listener)

        reader.contentHandler = object : DefaultHandler() {

            var txt: String = ""
            override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
                txt = ""
                currentTagHandler = currentTagHandler.startParsing(qName!!, attributes.toMap())

                super.startElement(uri, localName, qName, attributes)
            }

            override fun characters(ch: CharArray?, start: Int, length: Int) {
                ch?.run { txt += String(ch.copyOfRange(start, start + length)) }
            }

            override fun endElement(uri: String?, localName: String?, qName: String?) {
                currentTagHandler.consume(txt)
                currentTagHandler = currentTagHandler.endParsing()
            }
        }
        reader.parse(InputSource(File(file).reader()))
    }
}
