package tag.parsercombinators

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to create a context sensitive TAGML parser.
 * We need the basic open and close tags
 * Note that these are in package scope
 */
val openTagParser: Parser<Char, String> = char('[') thenRight charIn(CharRange('a', 'z')).rep thenLeft char('>') map {
    String(it.toCharArray())
}

fun expectedCloseTagParser(expected: String): Parser<Char, String> = char('<') thenRight string(expected) thenLeft char(']')

val anyOpenTagFollowedByTheExactSameCloseTagParser: Parser<Char, String> = {
    when (val result = openTagParser(it)) {
        is Response.Accept -> expectedCloseTagParser(result.value)(result.input)
        is Response.Reject -> result
    }
}

// legacy parsers
val anyCloseTagParser: Parser<Char, String> = char('<') thenRight charIn(CharRange('a', 'z')).rep thenLeft char(']') map {
    String(it.toCharArray())
}

val openAndCloseTagParser: Parser<Char, Pair<String, String>> = openTagParser then anyCloseTagParser

typealias DerivativeParser<I, P> = (reader: Reader<I>, pattern: P) -> P

val stringDerivativeParser: DerivativeParser<Char, String> = { reader, pattern ->
    val p = reader.read()
    val charThatIsRead = p?.first
    println(charThatIsRead)
    // TODO: check whether the first character is the same
    // then we return rest of the string
    // there is a first command and a drop command
    // this might not be the most efficient
    // but for now we continue working with this to get started
    val new_pattern = pattern.drop(1)
    new_pattern
}

fun main() {
// the following tests the pattern matchers for tagml open and close tags. There is not yet any context sensitive logic in there.
//    val a = Reader.string("[root><root]")
//    val r = anyOpenTagFollowedByTheExactSameCloseTagParser(a)
//    println(r)
//    val b = Reader.string("[root><wrong]")
//    val s = anyOpenTagFollowedByTheExactSameCloseTagParser(b)
//    println(s)

    // Test derivative parser
    // the string parser does nothing else than matching the string content character by character
    // return an empty expectation. Do we want to make a special type for that?
    // In any other case we should just return the string with the first character removed.
    // if the first char is wrong -> Reject
    // if first char is ok ->
    val a = Reader.string("This is just a string.")
    // in this case we make them exactly the same.
    val expectation = "This is just a string"
    val dp = stringDerivativeParser(a, expectation)
    println(dp)
}


/*
 * A need to have a parser with expectations.
 * After each parsing of a 'token' we need to rewrite our expectations based on what actually occurred.
 * That rewriting of the expectations is the hard part
 * In the haskell examples this is done based on types and pattern matching. Accorrding to the MEAP book
 * pattern matching is not as strong in Kotlin as it is in Scala.
 * Whether that becomes a problem is still an open question...
 *
 * The first expectation that we have is that there is an open tag.
 * then if that is matched,
 * */

data class Expectation<A>(val parser: Parser<Char, A>) {
    // TODO: do we want to store the expectation that should arise when this expectation is met?
    // If we do that, we create rules that move from expectation to expectation.
    // So we get we go from expectation to expectation
    // It is not that different from the current parser structure.
    // The difference is that we do not immediately execute the path.
}


