package tag.parsercombinators

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to create a context sensitive TAGML parser.
 */
// We need the basic open and close tags
// Note that these are in package scope
val openTagParser: Parser<Char, String> = char('[') thenRight charIn(CharRange('a', 'z')).rep thenLeft char('>') map {
    String(it.toCharArray())
}
val anyCloseTagParser: Parser<Char, String> = char('<').thenRight(charIn(CharRange('a', 'z')).rep).thenLeft(char(']')).map {
    String(it.toCharArray())
}
fun expectedCloseTagParser(expected: String): Parser<Char, String> = char('<') thenRight string(expected) thenLeft char(']')

val openAndCloseTagParser: Parser<Char, Pair<String, String>> = openTagParser then anyCloseTagParser


fun main() {
    val a = Reader.string("[root><root]")
    val r = openAndCloseTagParser(a)
    println(r)
}