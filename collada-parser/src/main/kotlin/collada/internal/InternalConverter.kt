package collada.internal

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

interface InternalConverter<T> {

    fun convert(document: Document): T
}