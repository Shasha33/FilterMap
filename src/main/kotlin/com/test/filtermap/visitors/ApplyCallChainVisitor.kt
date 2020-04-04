package com.test.filtermap.visitors

import com.test.parser.CallChainBaseVisitor
import com.test.parser.CallChainParser

class ApplyCallChainVisitor(initialList: List<Int>) : CallChainBaseVisitor<Unit>() {
    private var list = initialList
    val resultList : List<Int>
        get() = list.toList()

    override fun visitFilterCall(ctx: CallChainParser.FilterCallContext) {
        list = list.filter {
            ctx.expression().accept(LogicExpressionComputer(it))
        }
    }

    override fun visitMapCall(ctx: CallChainParser.MapCallContext) {
        list = list.map { ctx.expression().accept(ArithmeticExpressionComputer(it))
        }
    }
}