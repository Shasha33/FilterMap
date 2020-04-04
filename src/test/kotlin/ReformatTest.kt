package com.test.filtermap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReformatTest {
    private fun checkResultsOnEquals(initialLine: String, list: List<Int>) {
        val line = reformat(initialLine)
        checkFormat(line)
        assertEquals(apply(initialLine, list), apply(line, list))
    }

    private fun checkResultsOnEquals(initialLine: String, minValue: Int = -100, maxValue: Int = 100) {
        checkResultsOnEquals(initialLine, (minValue..maxValue).toList())
    }

    private fun checkFormat(result: String) {
        assertTrue(result.matches("filter\\{[^{}]*}%>%map\\{[^{}]*}".toRegex()))
    }

    @Test
    fun syntaxErrorOnEmptyStringTest() {
        assertThrows<SyntaxErrorException> {
            reformat("")
        }
    }

    @Test
    fun syntaxErrorOnIncorrectSeparatorsTest() {
        assertThrows<SyntaxErrorException> {
            reformat("map{(element+10)}%>filter{(element>10)}%>%map{(element*element)}")
        }
    }

    @Test
    fun syntaxErrorOnIncorrectOperationTest() {
        assertThrows<SyntaxErrorException> {
            reformat("map{(element/10)}")
        }
    }

    @Test
    fun syntaxErrorOnIncorrectFunctionNameTest() {
        assertThrows<SyntaxErrorException> {
            reformat("fmap{element}")
        }
    }

    @Test
    fun typeErrorOnIncorrectMapExpressionTest() {
        assertThrows<TypeErrorException> {
            reformat("map{(element<10)}")
        }
    }

    @Test
    fun typeErrorOnIncorrectFilterExpressionTest() {
        assertThrows<TypeErrorException> {
            reformat("filter{(1+element)}")
        }
    }

    @Test
    fun typeErrorOnIncorrectInnerExpressionTypeTest() {
        assertThrows<TypeErrorException> {
            reformat("filter{((1<element)<10)}")
        }
    }

    @Test
    fun filterTwiceExample() {
        checkResultsOnEquals("filter{(element>10)}%>%filter{(element<20)}")
    }

    @Test
    fun filterAfterMapExample() {
        checkResultsOnEquals("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}")
    }

    @Test
    fun emptyFilterResultExample() {
        checkResultsOnEquals("filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}")
    }
}