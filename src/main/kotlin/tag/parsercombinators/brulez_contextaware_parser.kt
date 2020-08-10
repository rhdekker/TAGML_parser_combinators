package tag.parsercombinators

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*
import java.io.File
import java.net.URL

// the idea here is simple
// We have a number of tagml markup parsers
// such as open tag, close tag....
// this parser returns a string but what we really want is to get back a MCT node.
val openTagNodeParser: Parser<Char, Markup> =
    char('[') thenRight charIn(CharRange('a', 'z')).rep thenLeft char('|') map {
    Markup(String(it.toCharArray()), listOf())
}

/*
 * First we need data classes for all parts of the MCT
 *
 */
sealed class Node
data class Markup(val label: String, val colors: List<String>, val id: Long = System.currentTimeMillis()) : Node()
data class Text(val content: String, val id: Long = System.currentTimeMillis()) : Node()

fun main() {
    val x = File(".")
    println(x.absolutePath)

    val a = Reader.url(URL("file:src/main/kotlin/tag/parsercombinators/brulez_no_annotations.tagml"))
    val r = openTagNodeParser(a)
    println(r)


//    val a = Reader.string("This is just a string.")
    // in this case we make them exactly the same.
//    val expectation = "This is just a string"
//    val dp = stringDerivativeParser(expectation)
//    val result = dp(a)
//    println(result)

}