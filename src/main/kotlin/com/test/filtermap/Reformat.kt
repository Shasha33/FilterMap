package com.test.filtermap

import com.test.filtermap.visitors.CollectCallsVisitor
import com.test.parser.CallChainLexer
import com.test.parser.CallChainParser
import org.antlr.v4.runtime.*

@Throws(TypeErrorException::class, SyntaxErrorException::class)
fun reformat(callChainLine: String): String {
    val simpleErrorListener = object : BaseErrorListener() {
        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInLine: Int,
            msg: String?,
            e: RecognitionException?
        ) {
            throw SyntaxErrorException()
        }
    }

    val lexer = CallChainLexer(CharStreams.fromString(callChainLine))
    lexer.removeErrorListeners()
    lexer.addErrorListener(simpleErrorListener)

    val parser = CallChainParser(CommonTokenStream(lexer))
    parser.removeErrorListeners()
    parser.addErrorListener(simpleErrorListener)

    val callChain = parser.callChain()
    val collector = CollectCallsVisitor()

    callChain.accept(collector)

    return collector.getCollectedString()
}