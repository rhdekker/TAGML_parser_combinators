import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

/*
 * Attempt to build a concur operator
 * 24-10-2019
 * Ronald Haentjens Dekker
 */

// function to build parsers with
// calls recursive function
fun concur(
    char: Parser<Char, Char>,
    char1: Parser<Char, Char>,
    char2: Parser<Char, Char>,
    char3: Parser<Char, Char>
): Parser<Char, List<Char>> {
    return { processConcur(char, char1, char2, char3, it) }
}

// TODO: incomplete implementation
fun <I, A> processConcur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>, reader: Reader<I>): Response<I, List<A>> {
    val pa = ((p1.then(p2))(reader) as Response.Accept).value
    return Response.Accept(listOf(pa.first, pa.second), reader, true)
}

fun main() {
    // we want to test the concur operator
    // input string
    val input = Reader.string("abcd")

    // Example specific parser
    val par: Parser<Char, List<Char>> = concur(char('a'), char('b'), char('c'), char('d'))

    // run the parser on the input
    val result = par(input)

    // print the result
    println(result)
}