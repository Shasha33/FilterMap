package com.test.filtermap.visitors

import com.test.filtermap.TypeErrorException
import com.test.parser.CallChainBaseVisitor
import com.test.parser.CallChainParser

class CollectCallsVisitor : CallChainBaseVisitor<Unit>() {
    private var mapExpression = "element"
    private var filterExpression = ""
    private val substituteVisitor = Substitute()

    override fun visitFilterCall(ctx: CallChainParser.FilterCallContext) {
        if (!ctx.expression().type().hasBoolResult()) {
            throw TypeErrorException()
        }
        val modifiedCondition = ctx.expression().accept(substituteVisitor)
        addFilterExpression(modifiedCondition)
    }

    override fun visitMapCall(ctx: CallChainParser.MapCallContext) {
        if (!ctx.expression().type().hasIntResult()) {
            throw TypeErrorException()
        }
        mapExpression = ctx.expression().accept(substituteVisitor)
    }

    private inner class Substitute : CallChainBaseVisitor<String>() {
        override fun visitBinaryExpression(ctx: CallChainParser.BinaryExpressionContext): String {
            if (!ctx.hasCorrectTypes()) {
                throw TypeErrorException()
            }

            val left = ctx.left.accept(this)
            val right = ctx.right.accept(this)
            return "($left${ctx.operation().text}$right)"
        }

        override fun visitConstantExpression(ctx: CallChainParser.ConstantExpressionContext): String {
            return ctx.NUMBER().text
        }

        override fun visitItExpression(ctx: CallChainParser.ItExpressionContext): String {
            return mapExpression
        }
    }

    private fun addFilterExpression(expression: String) {
        filterExpression = if (filterExpression.isEmpty()) {
            expression
        } else {
            "($filterExpression&$expression)"
        }
    }


    fun getCollectedString(): String {
        if (filterExpression.isEmpty()) {
            filterExpression = "(1=1)"
        }
        return "filter{$filterExpression}%>%map{$mapExpression}"
    }
}