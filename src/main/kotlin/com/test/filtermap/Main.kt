package com.test.filtermap

fun main() {
    val line = readLine() ?: return
    try {
        println(reformat(line))
    } catch (e: TypeErrorException) {
        println(e.message)
    } catch (e: SyntaxErrorException) {
        println(e.message)
    }
}