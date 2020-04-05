package com.test.filtermap.visitors

import com.test.filtermap.TypeErrorException
import com.test.parser.CallChainBaseVisitor
import com.test.parser.CallChainParser

/**
 * Call chain visitor reformatting all actions with list to one filter call and one map call.
 * Computes all arithmetic expressions with constant values.
 */
class CollectCallsVisitor : CallChainBaseVisitor<Unit>() {
    private var mapExpression = "element"
    private var filterExpression = ""
    private val substituteVisitor = Substitute()

    override fun visitFilterCall(ctx: CallChainParser.FilterCallContext) {
        if (!ctx.expression().type().hasBoolResult()) {
            throw TypeErrorException()
        }
        val modifiedCondition = ctx.expression().accept(substituteVisitor)
        addFilterExpression(modifiedCondition.result)
    }

    override fun visitMapCall(ctx: CallChainParser.MapCallContext) {
        if (!ctx.expression().type().hasIntResult()) {
            throw TypeErrorException()
        }
        mapExpression = ctx.expression().accept(substituteVisitor).result
    }

    private data class SubstituteResult(val isConstant: Boolean, val result: String)

    private inner class Substitute : CallChainBaseVisitor<SubstituteResult>() {

        override fun visitBinaryExpression(ctx: CallChainParser.BinaryExpressionContext): SubstituteResult {
            if (!ctx.hasCorrectTypes()) {
                throw TypeErrorException()
            }

            val left = ctx.left.accept(this)
            val right = ctx.right.accept(this)
            val leftResult = left.result
            val rightResult = right.result

            if (ctx.type() == ExpressionType.INT && left.isConstant && right.isConstant) {
                val result = when(ctx.operation().text) {
                    "+" -> leftResult.toInt() + rightResult.toInt()
                    "-" -> leftResult.toInt() - rightResult.toInt()
                    "*" -> leftResult.toInt() * rightResult.toInt()
                    else -> throw TypeErrorException()
                }
                return SubstituteResult(true, result.toString())
            }

            return SubstituteResult(false, "($leftResult${ctx.operation().text}$rightResult)")
        }

        override fun visitConstantExpression(ctx: CallChainParser.ConstantExpressionContext): SubstituteResult {
            return SubstituteResult(true, ctx.NUMBER().text)
        }

        override fun visitItExpression(ctx: CallChainParser.ItExpressionContext): SubstituteResult {
            return SubstituteResult(false, mapExpression)
        }
    }

    private fun addFilterExpression(expression: String) {
        filterExpression = if (filterExpression.isEmpty()) {
            expression
        } else {
            "($filterExpression&$expression)"
        }
    }


    /**
     * Joins collected expressions to new correct call chain.
     */
    fun getCollectedString(): String {
        if (filterExpression.isEmpty()) {
            filterExpression = "(1=1)"
        }
        return "filter{$filterExpression}%>%map{$mapExpression}"
    }
}