package com.test.filtermap

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReformatTest {
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
}