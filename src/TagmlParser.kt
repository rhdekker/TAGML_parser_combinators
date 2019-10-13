import lambdada.parsec.parser.* // combinators, e.g. string, char, not, ...
import lambdada.parsec.parser.Response.* // for reading the parser result (Accept, Reject)
import lambdada.parsec.io.Reader // for running parsers (Reader)


fun main(args: Array<String>) {
// The example for a `Parser<Char, List<String>>`
    val foo: Parser<Char, List<Char>> = not(char(',')).rep
    val input = Reader.string("hello, parsec!")
    val result = foo(input)

    when (result) {
        is Accept -> println("good")
        is Reject -> println("bad")
    }
    // good
}
