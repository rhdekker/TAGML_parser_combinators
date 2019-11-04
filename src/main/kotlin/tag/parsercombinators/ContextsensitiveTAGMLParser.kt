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

/*
 Attempt to create a parser derivative approach
 */

typealias DerivativeParser<I, P> = (reader: Reader<I>, pattern: P) -> P



// TODO: check for end of file or IO error
// TODO: check whether the first character is the same
// then we return rest of the string
// there is a first command and a drop command
// this might not be the most efficient
// but for now we continue working with this to get started

//val stringDerivativeParser: DerivativeParser<Char, String> = { reader, pattern ->
//    val p = reader.read()
//    val charThatIsRead = p?.first
//    println(charThatIsRead)
//    val new_pattern = pattern.drop(1)
//    new_pattern
//}

// In case of Accept I just return an empty string
// In case Of Reject; we are not able to express that yet; we need a better data structure
val stringDerivativeParser: DerivativeParser<Char, String> = { reader, pattern ->
    val delegateFunction = string(pattern)
    when (delegateFunction(reader)) {
        is Response.Accept -> ""
        is Response.Reject -> "String is wrong!"
    }
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



// Implementation requirements
// now we need to do repeat
// a repeat is it has to do once and then optionally multiple times after that
// we need to check for a character in a certain group
// optional first...
// optional is to check whether something occurs if not... that is ok.. move on..
// if it does not have a next parser... we just end...
// we need to set back the position..
// Can't we just wrap an existing parser...
// Maybe... the then or "and" must be different...
// the or could maybe work
// we don't want parser to call other parsers...

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


