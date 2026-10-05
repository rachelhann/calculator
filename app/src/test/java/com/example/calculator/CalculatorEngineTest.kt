package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * CalculatorEngine has no Android dependency, so its math and formatting
 * rules are verified here as plain local unit tests.
 */
class CalculatorEngineTest {

    @Test
    fun addition_isCorrect() {
        assertEquals("15", CalculatorEngine.evaluate("9+6"))
    }

    @Test
    fun subtraction_isCorrect() {
        assertEquals("4", CalculatorEngine.evaluate("10-6"))
    }

    @Test
    fun multiplication_isCorrect() {
        assertEquals("42", CalculatorEngine.evaluate("6*7"))
    }

    @Test
    fun division_isCorrect() {
        assertEquals("4", CalculatorEngine.evaluate("12/3"))
    }

    @Test
    fun decimalValues_areSupported() {
        assertEquals("2.5", CalculatorEngine.evaluate("5/2"))
    }

    @Test
    fun divisionByZero_showsDedicatedMessage() {
        assertEquals(CalculatorEngine.DIVIDE_BY_ZERO, CalculatorEngine.evaluate("5/0"))
    }

    @Test
    fun invalidExpression_showsError() {
        assertEquals(CalculatorEngine.ERROR, CalculatorEngine.evaluate("5+*3"))
    }

    @Test
    fun incompleteExpression_isDetected() {
        assertEquals(true, CalculatorEngine.isIncomplete("9+"))
        assertEquals(false, CalculatorEngine.isIncomplete("9+6"))
        assertEquals(true, CalculatorEngine.isIncomplete(""))
    }

    @Test
    fun toggleSign_negatesTrailingNumber() {
        assertEquals("12+-5", CalculatorEngine.toggleSign("12+5"))
    }

    @Test
    fun toggleSign_undoesNegation() {
        assertEquals("12+5", CalculatorEngine.toggleSign("12+-5"))
    }

    @Test
    fun toggleSign_onEmptyEquation_startsNegativeNumber() {
        assertEquals("-", CalculatorEngine.toggleSign(""))
    }

    @Test
    fun applyPercent_dividesByOneHundred() {
        assertEquals("0.5", CalculatorEngine.evaluate(CalculatorEngine.applyPercent("50")))
    }

    @Test
    fun formatForDisplay_addsSpacesAroundOperators() {
        assertEquals("9 + 6", CalculatorEngine.formatForDisplay("9+6"))
    }
}
