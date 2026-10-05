package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel(){

    private val _equationText = MutableLiveData("")
    val equationText : LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText : LiveData<String> = _resultText

    private val _history = MutableLiveData<List<String>>(emptyList())
    val history : LiveData<List<String>> = _history

    fun clearHistory() {
        _history.value = emptyList()
    }

    fun onButtonClick(btn : String){
        Log.i("Clicked button", btn)

        _equationText.value?.let {
            if(btn=="AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }

            if(btn=="C") {
                if(it.isNotEmpty()){
                    _equationText.value = it.substring(0, it.length-1)
                    updateResult(_equationText.value.orEmpty())
                }
                return
            }

            if(btn == "=") {
                val result = _resultText.value
                if (it.isNotBlank() && !isError(result)) {
                    _history.value = _history.value.orEmpty() + "${formatEquation(it)} = $result"
                }
                // if the current result is an error, start fresh instead of
                // carrying the broken expression forward
                _equationText.value = if (isError(result)) "" else result
                return
            }

            if(btn == "+/-") {
                _equationText.value = toggleSign(it)
                updateResult(_equationText.value.orEmpty())
                return
            }

            if(btn == "%") {
                // percentage: divides the current running value by 100
                _equationText.value = "($it)/100"
                updateResult(_equationText.value.orEmpty())
                return
            }

            // concatenate
            _equationText.value = it+btn
            updateResult(_equationText.value.orEmpty())
        }

    }

    private fun isError(result: String?) = result == "Error" || result == "Cannot divide by zero"

    // adds spaces around binary operators for display, e.g. "9+6" -> "9 + 6"
    private fun formatEquation(equation: String): String =
        equation.replace(Regex("(?<=[0-9)])([+\\-*/])")) { " ${it.value} " }

    // flips the sign of the number currently being entered, e.g. "12+5" -> "12+-5"
    private fun toggleSign(equation: String): String {
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

    private fun updateResult(equation: String) {
        if (equation.isBlank()) {
            _resultText.value = "0"
            return
        }
        if (equation.last() in "+-*/") {
            // equation is incomplete (ends in an operator waiting for the next
            // number) - keep showing the previous result instead of erroring
            return
        }
        try {
            _resultText.value = calculateResult(equation)
        } catch (_: Exception) {
            _resultText.value = "Error"
        }
    }

    // calculate results
    fun calculateResult(equation : String) : String{
        val context : Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable : Scriptable = context.initStandardObjects()
        val raw = context.evaluateString(scriptable,equation,"Javascript",1,null).toString()
        if (raw == "NaN") return "Error"
        if (raw == "Infinity" || raw == "-Infinity") return "Cannot divide by zero"
        var finalResult = raw
        if(finalResult.endsWith(".0")){
            finalResult = finalResult.replace(".0", "")
        }
        return finalResult
    }
}