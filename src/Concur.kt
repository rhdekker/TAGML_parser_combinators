import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to build a concur operator
 * 24-10-2019
 * Ronald Haentjens Dekker
 */

// Function to build parsers with.
// Should call recursive function.
fun concur(char: Parser<Char, Char>, char1: Parser<Char, Char>, char2: Parser<Char, Char>, char3: Parser<Char, Char>):
        Parser<Char, List<Char>> = { processConcur(char, char1, char2, char3, it) }

// TODO: incomplete implementation
fun <I, A> processConcur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>, reader: Reader<I>): Response<I, List<A>> {
    val pa = ((p1.then(p2))(reader) as Response.Accept)
    val pa2 = ((p3.then(p4))(pa.input) as Response.Accept).value
    return Response.Accept(listOf(pa.value.first, pa.value.second, pa2.first, pa2.second), reader, true)
}

// TODO: trying to make it work with a list
fun <I, A> processConcur2(parsers: List<Parser<I, A>>, reader: Reader<I>): Response<I, List<A>> {
    val firstParser = parsers.first()
    val remainder = parsers.subList(1, parsers.size)
    val result = firstParser(reader)
    val check = result is Response.Accept && !remainder.isEmpty()
    return Unit as Response<I, List<A>>
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