package com.test.filtermap

import com.test.filtermap.visitors.ApplyCallChainVisitor
import com.test.filtermap.visitors.CollectCallsVisitor
import com.test.parser.CallChainLexer
import com.test.parser.CallChainParser
import org.antlr.v4.runtime.*

private fun parseCallChain(callChainLine: String): CallChainParser.CallChainContext {
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
    return parser.callChain()
}

/**
 * Reformats call chain to one filter call and one map call.
 */
@Throws(TypeErrorException::class, SyntaxErrorException::class)
fun reformat(callChainLine: String): String {
    val callChain = parseCallChain(callChainLine)
    val collector = CollectCallsVisitor()

    callChain.accept(collector)

    return collector.getCollectedString()
}

/**
 * Applies call chain to the list.
 */
@Throws(TypeErrorException::class, SyntaxErrorException::class)
fun apply(callChainLine: String, list: List<Int>): List<Int> {
    val callChain = parseCallChain(callChainLine)
    val applyVisitor = ApplyCallChainVisitor(list)
    callChain.accept(applyVisitor)
    return applyVisitor.resultList
}