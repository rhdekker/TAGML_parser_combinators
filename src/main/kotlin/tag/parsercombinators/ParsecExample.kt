package tag.parsercombinators

import lambdada.parsec.io.Reader
import lambdada.parsec.parser.*

infix fun <I, A> Parser<I, A>.after(p: Parser<I, A>): Parser<I, List<A>> = this flatMap { a -> p map { listOf(a, it) } }

infix fun <I, A> Parser<I, List<A>>.after2(p: Parser<I, A>): Parser<I, List<A>> = this flatMap { a -> p map { a + it } }

fun main() {
//    val foo: Parser<Char, List<Char>> = not(char(',')).rep
    val foo: Parser<Char, List<Char>> = char('h') after char('e') after2 char('l')
    val input = Reader.string("hello, parsec!")
    val foobar = foo(input)
    println(foobar)

    when (foobar) {
        is Response.Accept -> println("good")
        is Response.Reject -> println("bad")
    }
    // good
}