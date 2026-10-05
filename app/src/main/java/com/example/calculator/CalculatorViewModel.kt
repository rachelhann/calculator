package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Holds the calculator's UI state and dispatches button presses. Actual math
 * and string formatting live in [CalculatorEngine], which has no Android or
 * Compose dependency and can be tested on its own.
 */
class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    private val _history = MutableLiveData<List<String>>(emptyList())
    val history: LiveData<List<String>> = _history

    fun clearHistory() {
        _history.value = emptyList()
    }

    fun onButtonClick(btn: String) {
        Log.i("Clicked button", btn)
        val equation = _equationText.value ?: return

        when (btn) {
            "AC" -> clearAll()
            "C" -> backspace(equation)
            "=" -> evaluateAndRecordHistory(equation)
            "+/-" -> applyTransform(CalculatorEngine.toggleSign(equation))
            "%" -> applyTransform(CalculatorEngine.applyPercent(equation))
            else -> applyTransform(equation + btn)
        }
    }

    private fun clearAll() {
        _equationText.value = ""
        _resultText.value = "0"
    }

    private fun backspace(equation: String) {
        if (equation.isEmpty()) return
        val trimmed = equation.substring(0, equation.length - 1)
        _equationText.value = trimmed
        updateResult(trimmed)
    }

    private fun evaluateAndRecordHistory(equation: String) {
        val result = _resultText.value
        if (equation.isNotBlank() && !CalculatorEngine.isError(result)) {
            _history.value = _history.value.orEmpty() +
                "${CalculatorEngine.formatForDisplay(equation)} = $result"
        }
        // if the current result is an error, start fresh instead of carrying
        // the broken expression forward
        _equationText.value = if (CalculatorEngine.isError(result)) "" else result
    }

    private fun applyTransform(newEquation: String) {
        _equationText.value = newEquation
        updateResult(newEquation)
    }

    private fun updateResult(equation: String) {
        _resultText.value = when {
            equation.isBlank() -> "0"
            // equation ends in an operator awaiting the next number - keep
            // showing the previous result instead of erroring
            CalculatorEngine.isIncomplete(equation) -> return
            else -> CalculatorEngine.evaluate(equation)
        }
    }
}
