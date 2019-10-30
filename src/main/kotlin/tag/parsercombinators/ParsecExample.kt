package tag.parsercombinators

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

fun main() {
    val foo: Parser<Char, List<Char>> = not(char(',')).rep
    val input = Reader.string("hello, parsec!")
    val foobar = foo(input)
    println(foobar)

    when (foobar) {
        is Response.Accept -> println("good")
        is Response.Reject -> println("bad")
    }
    // good
}