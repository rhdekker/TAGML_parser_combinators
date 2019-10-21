import lambdada.parsec.parser.* // combinators, e.g. string, char, not, ...
import lambdada.parsec.parser.Response.* // for reading the parser result (Accept, Reject)
import lambdada.parsec.io.Reader // for running parsers (Reader)


/* trying to build a TAGML parser using parser combinators in Kotlin
  13-10-2019

  Ronald Haentjens Dekker
 */

data class MCTNode(val name: String)
data class OpenMCTNode(val name: String, val open: Boolean = true)
data class ClosedMCTNode(val name: String, val open: Boolean = false)

fun main() {
    // basic TAGML parsers
    val opentagParser: Parser<Char, OpenMCTNode> = char('[').thenRight(charIn(CharRange('A', 'z')).rep).thenLeft(char('>')).map { OpenMCTNode (String(it.toCharArray())) }
    val closeTagParser: Parser<Char, ClosedMCTNode> = char('<').thenRight(charIn(CharRange('A', 'z')).rep).map { ClosedMCTNode(String(it.toCharArray()))}

        //.thenLeft(char(']'))

    // close tag test
    val testCloseTag = Reader.string("<child]")
    val mytestparser: Parser<Char, ClosedMCTNode> = closeTagParser
    val resultt = mytestparser(testCloseTag)
    println(resultt)

    // We want to return the root node, which has a child node
    val moreComplexTAGML = Reader.string("[root>[child><child]<root]")
    val myparser: Parser<Char, Pair<Pair<OpenMCTNode, OpenMCTNode>, ClosedMCTNode>> = opentagParser.then(opentagParser).then(closeTagParser)
    val result = myparser(moreComplexTAGML)

    println(result)

    // once we get a close tag we need to reduce a

}


fun test() {
    val foo: Parser<Char, List<Char>> = not(char(',')).rep
    val input = Reader.string("hello, parsec!")
    val foobar = foo(input)
    println(foobar)

    when (foobar) {
        is Accept -> println("good")
        is Reject -> println("bad")
    }
    // good

    val tagml = Reader.string("[tagml>")
    val tagmlParser: Parser<Char, MCTNode> = string("[tagml>").map { f -> MCTNode("tagml") }
    val result = tagmlParser(tagml)
    println(result)

    val identifierTest = Reader.string("test122343")
    val identifier: Parser<Char, List<Char>> = (charIn(CharRange('0', 'z'))).rep
    val result2 = identifier(identifierTest)
    println(result2)


}