import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to build a concur operator
 * 24-10-2019
 * Ronald Haentjens Dekker
 */

// Function to build parsers with.
fun <I, A> concur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>):
        Parser<I, List<A>> = { processConcurRecursive(listOf(p1, p2, p3, p4), listOf(), it) }

// we go over all the parsers looking for an Accept.
// we map all the parsers to a (parser, response) pair
// then we filter to the first accept
// if no parser was found -> reject
// if a parser was found and no more parsers are to do -> accept
// if a parser was found and after filtering list of parsers not empty -> recurse
// Note: if we no parsers are supplied to this function it will reject.
// TODO: consumed is always false in case of a reject. That does not have to be the case, one or more parsers could have succeeded!
tailrec fun <I, A> processConcurRecursive(parsers: List<Parser<I, A>>, results: List<A>, reader: Reader<I>): Response<I, List<A>> {
    val firstParserResponsePair =
        parsers.map { parser -> Pair(parser, parser(reader)) }.find { pair -> pair.second is Response.Accept }

    val response = firstParserResponsePair?.second as Response.Accept<I, A>?
        ?: return Response.Reject(reader.location(), false)

    val parsersToDo = parsers.filter { parser -> parser != firstParserResponsePair?.first }

    return when (parsersToDo.isEmpty()) {
        true -> Response.Accept(results + response.value, response.input, true)
        false -> processConcurRecursive(parsersToDo, results + response.value, response.input)
    }
}

fun main() {
    // we want to test the concur operator
    // input string
    val input = Reader.string("abcd")

    // Example specific parser
    val par: Parser<Char, List<Char>> = concur(char('a'), char('b'), char('c'), char('d'))

    // run the parser on the input
    val result = par(input)
    println(result)

    // second test case
    val input2 = Reader.string("dcba")
    val result2 = par(input2)
    println(result2)
}