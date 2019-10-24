import lambdada.parsec.io.Reader
import lambdada.parsec.parser.Parser
import lambdada.parsec.parser.Response
import lambdada.parsec.parser.char
import lambdada.parsec.parser.returns

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
): Parser<Char, Boolean> {
    // temp
    return Unit as Parser<Char, Boolean>
}

//fun <I, A> processConcur(p1: Parser<I, A>, p2: Parser<I, A>, p3: Parser<I, A>, p4: Parser<I, A>, reader: Reader<I>): Response<I, A> {
//    Acce
//}

fun main() {
    // we want to test the concur operator
    // input string
    val input = Reader.string("abcd")

    // Example specific parser
    val par: Parser<Char, Boolean> = concur(char('a'), char('b'), char('c'), char('d'))

    // run the parser on the input
    val result = par(input)

    // print the result
    println(result)
}