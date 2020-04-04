package com.test.filtermap.visitors

import com.test.filtermap.TypeErrorException
import com.test.parser.CallChainBaseVisitor
import com.test.parser.CallChainParser

internal class ArithmeticExpressionComputer(private val itValue: Int) : CallChainBaseVisitor<Int>() {
    override fun visitBinaryExpression(ctx: CallChainParser.BinaryExpressionContext): Int {
        val left = ctx.left.accept(this)
        val right = ctx.right.accept(this)
        return when (ctx.operation().text) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            else -> throw TypeErrorException()
        }
    }

    override fun visitItExpression(ctx: CallChainParser.ItExpressionContext?): Int {
        return itValue
    }

    override fun visitConstantExpression(ctx: CallChainParser.ConstantExpressionContext): Int {
        return ctx.NUMBER().text.toInt()
    }
}

internal class LogicExpressionComputer(itValue: Int) : CallChainBaseVisitor<Boolean>() {
    private val arithmeticExpressionComputer =
        ArithmeticExpressionComputer(itValue)

    override fun visitBinaryExpression(ctx: CallChainParser.BinaryExpressionContext): Boolean {
        val operation = ctx.operation().text
        return if (ctx.type() == ExpressionType.BOOL) {
            computeLogicOperation(operation, ctx.left, ctx.right)
        } else {
            computeCompareExpression(operation, ctx.left, ctx.right)
        }
    }

    private fun computeCompareExpression(
        operation: String,
        leftCtx: CallChainParser.ExpressionContext,
        rightCtx: CallChainParser.ExpressionContext
    ): Boolean {
        val left = leftCtx.accept(arithmeticExpressionComputer)
        val right = rightCtx.accept(arithmeticExpressionComputer)
        return when (operation) {
            "<" -> left < right
            ">" -> left > right
            "=" -> left == right
            else -> throw TypeErrorException()
        }
    }

    private fun computeLogicOperation(
        operation: String,
        leftCtx: CallChainParser.ExpressionContext,
        rightCtx: CallChainParser.ExpressionContext
    ): Boolean {
        val left = leftCtx.accept(this)
        val right = rightCtx.accept(this)
        return when (operation) {
            "&" -> left and right
            "|" -> left or right
            else -> throw TypeErrorException()
        }
    }
}
