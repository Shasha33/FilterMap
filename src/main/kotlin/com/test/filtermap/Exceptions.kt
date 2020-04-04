package com.test.filtermap

import java.lang.Exception

/**
 * Exception throws in case of type mismatch in call chain reformatting or applying.
 */
class TypeErrorException : Exception("TYPE ERROR")

/**
 * Exception throws in case of parsing error in call chain reformatting or applying.
 */
class SyntaxErrorException : Exception("SYNTAX ERROR")