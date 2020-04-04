package com.test.filtermap.visitors

import com.test.parser.CallChainParser


enum class ExpressionType {
    IT, CONST, BOOL, INT, COMP;

    fun hasIntResult(): Boolean {
        return this == IT || this == CONST || this == INT
    }

    fun hasBoolResult(): Boolean {
        return this == BOOL || this == COMP
    }
}

fun CallChainParser.ExpressionContext.type(): ExpressionType {
    return listOfNotNull(itExpression()?.type(), constantExpression()?.type(), binaryExpression()?.type()).single()
}

fun CallChainParser.ItExpressionContext.type(): ExpressionType {
    return ExpressionType.IT
}

fun CallChainParser.ConstantExpressionContext.type(): ExpressionType {
    return ExpressionType.CONST
}

fun CallChainParser.BinaryExpressionContext.type(): ExpressionType {
    if (operation().text in listOf("&", "|")) {
        return ExpressionType.BOOL
    }
    if (operation().text in listOf("<", ">", "=")) {
        return ExpressionType.COMP
    }
    return ExpressionType.INT
}

/**
 * Check binary expression operands has correct type to apply operation.
 * int and int for arithmetic and comparison operations and
 * bool and bool for logical.
 */
fun CallChainParser.BinaryExpressionContext.hasCorrectTypes(): Boolean {
    val leftType = left.type()
    val rightType = right.type()
    return when (type()) {
        ExpressionType.COMP, ExpressionType.INT -> leftType.hasIntResult() && rightType.hasIntResult()
        ExpressionType.BOOL -> leftType.hasBoolResult() && rightType.hasBoolResult()
        else -> false
    }
}