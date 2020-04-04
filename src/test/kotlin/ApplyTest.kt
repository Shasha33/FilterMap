package com.test.filtermap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApplyTest {
    private fun checkResultOn(callChain: String, list: List<Int>, expectedResult: List<Int>) {
        val result = apply(callChain, list)
        assertEquals(expectedResult, result)
    }

    @Test
    fun singleFilterApplyTest() {
        checkResultOn("filter{(element>10)}", (0..20).toList(), (11..20).toList())
    }

    @Test
    fun singleMapApplyTest() {
        checkResultOn("map{(element+10)}", (0..20).toList(), (10..30).toList())
    }

    @Test
    fun filterMapApplyTest() {
        checkResultOn("filter{(element>10)}%>%map{(element+10)}", (0..20).toList(), (21..30).toList())
    }

    @Test
    fun mapFilterApplyTest() {
        checkResultOn("map{(element+10)}%>%filter{(element>10)}", (0..20).toList(), (11..30).toList())
    }

    @Test
    fun filterWithInnerExpressionsApplyTest() {
        checkResultOn("filter{((element*10)>(10+100))}", (0..20).toList(), (12..20).toList())
    }

    @Test
    fun mapWithSeveralElementUsageExpressionsApplyTest() {
        checkResultOn("map{(element*(element*element))}", (0..4).toList(), listOf(0, 1, 8, 27, 64))
    }

    @Test
    fun subMapApplyTest() {
        checkResultOn("map{(element-10)}", (0..4).toList(), (-10..-6).toList())
    }

    @Test
    fun addMapApplyTest() {
        checkResultOn("map{(element+10)}", (0..4).toList(), (10..14).toList())
    }

    @Test
    fun multMapApplyTest() {
        checkResultOn("map{(element*10)}", (0..4).toList(), listOf(0, 10, 20, 30, 40))
    }

    @Test
    fun ltFilterApplyTest() {
        checkResultOn("filter{(element<10)}", (0..20).toList(), (0..9).toList())
    }

    @Test
    fun gtFilterApplyTest() {
        checkResultOn("filter{(element>10)}", (0..20).toList(), (11..20).toList())
    }

    @Test
    fun eqFilterApplyTest() {
        checkResultOn("filter{(element=10)}", (0..20).toList(), listOf(10))
    }

    @Test
    fun andFilterApplyTest() {
        checkResultOn("filter{((element<10)&(8<element))}", (0..20).toList(), listOf(9))
    }

    @Test
    fun orFilterApplyTest() {
        checkResultOn("filter{((element<10)|(8<element))}", (0..20).toList(), (0..20).toList())
    }

    @Test
    fun idMapApplyTest() {
        checkResultOn("map{element}", (0..20).toList(), (0..20).toList())
    }

    @Test
    fun constMapApplyTest() {
        checkResultOn("map{10}", (0..20).toList(), List(21) {10})
    }

    @Test
    fun constFilterApplyTest() {
        checkResultOn("filter{(1<0)}", (0..20).toList(), emptyList())
    }

    @Test
    fun longCallChainTest() {
        checkResultOn(
            "map{(element-1)}%>%filter{(element>0)}%>%map{(element+2)}%>%filter{(element=3)}%>%map{(element*10)}",
            (-10..20).toList(), listOf(30)
        )
    }
}