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
val anyCloseTagParser: Parser<Char, String> = char('<') thenRight charIn(CharRange('a', 'z')).rep thenLeft char(']') map {
    String(it.toCharArray())
}
fun expectedCloseTagParser(expected: String): Parser<Char, String> = char('<') thenRight string(expected) thenLeft char(']')

val openAndCloseTagParser: Parser<Char, Pair<String, String>> = openTagParser then anyCloseTagParser

val anyOpenTagFollowedByTheExactSameCloseTagParser: Parser<Char, String> = {
    val result = openTagParser(it)
    //NOTE: nicer to use when instead of cast
    val expectedString = (result as Response.Accept).value
    val readerFromResponse = result.input
    expectedCloseTagParser(expectedString)(readerFromResponse)
}

fun main() {
    val a = Reader.string("[root><root]")
    val r = anyOpenTagFollowedByTheExactSameCloseTagParser(a)
    println(r)
    val b = Reader.string("[root><wrong]")
    val s = anyOpenTagFollowedByTheExactSameCloseTagParser(b)
    println(s)
}