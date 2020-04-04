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

    @Test
    fun severalMapsTest() {
        checkResultsOnEquals("map{(element-1)}%>%map{0}%>%map{(element*element)}")
    }

    @Test
    fun severalFiltersTest() {
        checkResultsOnEquals(
            "filter{(10>element)}%>%filter{((element<0)|(element>0))}%>%filter{((element*element)>1)}"
        )
    }

    @Test
    fun negativeConstantsTest() {
        checkResultsOnEquals(
            "map{((element*100)+(-100+element))}%>%map{(element*-10)}%>%filter{((element*element)>-2)}"
        )
    }

    @Test
    fun singleFilterTest() {
        checkResultsOnEquals("filter{(element>0)}")
    }

    @Test
    fun singleMapTest() {
        checkResultsOnEquals("map{(2-element)}")
    }

    @Test
    fun filterMapTest() {
        checkResultsOnEquals("filter{(element=1)}%>%map{(2*element)}")
    }

    @Test
    fun mapFilterTest() {
        checkResultsOnEquals("map{(element+10)}%>%filter{(element>10)}")
    }

    @Test
    fun filterWithInnerExpressionsTest() {
        checkResultsOnEquals("filter{((element*10)>(10+100))}")
    }

    @Test
    fun mapWithInnerExpressionsTest() {
        checkResultsOnEquals("map{((element*10)-(10+(element*element)))}")
    }

    @Test
    fun singleSubMapTest() {
        checkResultsOnEquals("map{(element-10)}")
    }

    @Test
    fun singleAddMapTest() {
        checkResultsOnEquals("map{(element+10)}")
    }

    @Test
    fun singleMultMapTest() {
        checkResultsOnEquals("map{(element*10)}")
    }

    @Test
    fun singleLtFilterTest() {
        checkResultsOnEquals("filter{(element<10)}")
    }

    @Test
    fun singleGtFilterTest() {
        checkResultsOnEquals("filter{(element>10)}")
    }

    @Test
    fun singleEqFilterTest() {
        checkResultsOnEquals("filter{(element=10)}")
    }

    @Test
    fun singleAndFilterTest() {
        checkResultsOnEquals("filter{((element<10)&(8<element))}")
    }

    @Test
    fun singleOrFilterTest() {
        checkResultsOnEquals("filter{((element<10)|(8<element))}")
    }

    @Test
    fun constMapTest() {
        checkResultsOnEquals("map{((2+10)*(40-11))}")
    }

    @Test
    fun onlyElementExpressionMapTest() {
        checkResultsOnEquals("map{((element+element)*element)}")
    }

    @Test
    fun constFilterTest() {
        checkResultsOnEquals("filter{(((100+11)>(300-123))&((300+123)<1000))}")
    }

    @Test
    fun onlyElementExpressionFilterTest() {
        checkResultsOnEquals("filter{((element=element)&((element<0)|(element>100)))}")
    }
}