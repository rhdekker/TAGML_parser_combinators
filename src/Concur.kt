import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to build a concur operator
 * 24-10-2019
 * Ronald Haentjens Dekker
 */

// Function to build parsers with.
// Should call recursive function.
// was: processConcur(p1, p2, p3, p4, it)
fun <I, A> concur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>):
        Parser<I, List<A>> = { processConcur2(listOf(p1, p2, p3, p4), listOf(), it) }

// TODO: incomplete implementation
fun <I, A> processConcur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>, reader: Reader<I>): Response<I, List<A>> {
    val pa = p1.then(p2)(reader) as Response.Accept
    val pa2 = (p3.then(p4)(pa.input) as Response.Accept).value
    return Response.Accept(listOf(pa.value.first, pa.value.second, pa2.first, pa2.second), reader, true)
}

// TODO: trying to make it work with a list
tailrec fun <I, A> processConcur2(parsers: List<Parser<I, A>>, results: List<A>, reader: Reader<I>): Response<I, List<A>> {
    // we go over all the parsers looking for an Accept.
    // we map all the parsers to a (parser, response) pair
    // then we filter to the first accept
    val firstParserResponsePair =
        parsers.map { parser -> Pair(parser, parser(reader)) }.find { pair -> pair.second is Response.Accept }

    println(firstParserResponsePair)

    // if no parser was found -> reject
    val response = firstParserResponsePair?.second as Response.Accept<I, A>?
        ?: return Response.Reject(reader.location(), false)

    // if a parser was found and no more parsers are to do -> accept
    // if a parser was found and after filtering list of parsers not empty -> recurse
    val parsersToDo = parsers.filter { parser -> parser != firstParserResponsePair?.first }

    return when (parsersToDo.isEmpty()) {
        true -> Response.Accept(results + response.value, response.input, true)
        false -> processConcur2(parsersToDo, results + response.value, response.input)
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