package com.example.calculator

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

/**
 * All calculator math and expression formatting, kept free of Android and
 * Compose dependencies so it can be unit tested and reused independently of
 * the UI (see CalculatorEngineTest).
 */
object CalculatorEngine {

    const val ERROR = "Error"
    const val DIVIDE_BY_ZERO = "Cannot divide by zero"

    fun isError(result: String?) = result == ERROR || result == DIVIDE_BY_ZERO

    /** An expression ending in an operator (or empty) has no value yet. */
    fun isIncomplete(equation: String) = equation.isEmpty() || equation.last() in "+-*/"

    fun evaluate(equation: String): String = try {
        val context: Context = Context.enter()
        try {
            context.optimizationLevel = -1
            val scriptable: Scriptable = context.initStandardObjects()
            val raw = context.evaluateString(scriptable, equation, "Calculator", 1, null).toString()
            when {
                raw == "NaN" -> ERROR
                raw == "Infinity" || raw == "-Infinity" -> DIVIDE_BY_ZERO
                raw.endsWith(".0") -> raw.removeSuffix(".0")
                else -> raw
            }
        } finally {
            Context.exit()
        }
    } catch (_: Exception) {
        ERROR
    }

    /** Flips the sign of the number currently being entered, e.g. "12+5" -> "12+-5". */
    fun toggleSign(equation: String): String {
        if (equation.isEmpty()) return "-"
        val match = Regex("\\d+\\.?\\d*$").find(equation) ?: return equation
        val numberStart = match.range.first
        val minusIndex = numberStart - 1
        if (minusIndex >= 0 && equation[minusIndex] == '-') {
            val beforeMinusIndex = minusIndex - 1
            val isUnaryMinus = beforeMinusIndex < 0 || equation[beforeMinusIndex] in "+-*/"
            if (isUnaryMinus) {
                return equation.removeRange(minusIndex, minusIndex + 1)
            }
        }
        return equation.substring(0, numberStart) + "-" + equation.substring(numberStart)
    }

    /** Percentage: divides the current running value by 100. */
    fun applyPercent(equation: String): String = "($equation)/100"

    /** Adds spaces around binary operators for display, e.g. "9+6" -> "9 + 6". */
    fun formatForDisplay(equation: String): String =
        equation.replace(Regex("(?<=[0-9)])([+\\-*/])")) { " ${it.value} " }
}
