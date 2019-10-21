import lambdada.parsec.parser.* // combinators, e.g. string, char, not, ...
import lambdada.parsec.parser.Response.* // for reading the parser result (Accept, Reject)
import lambdada.parsec.io.Reader // for running parsers (Reader)


/* trying to build a TAGML parser using parser combinators in Kotlin
  13-10-2019

  Ronald Haentjens Dekker
 */

data class MCTNode(val name: String)
data class OpenMCTNode(val name: String, val open: Boolean = true)

fun main() {
    // We want to return the root node, which has a child node
    val moreComplexTAGML = Reader.string("[root>[child><child]<root]")
    val opentagParser: Parser<Char, OpenMCTNode> = char('[').thenRight(charIn(CharRange('A', 'z')).rep).thenLeft(char('>')).map { OpenMCTNode (String(it.toCharArray())) }
    val myparser: Parser<Char, Pair<OpenMCTNode, OpenMCTNode>> = opentagParser.then(opentagParser)
    val result = myparser(moreComplexTAGML)

    println(result)




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