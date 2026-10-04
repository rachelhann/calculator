package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel(){

    private val _equationText = MutableLiveData("")
    val equationText : LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText : LiveData<String> = _resultText
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
                    return
                }
            }

            if(btn == "=") {
                _equationText.value = _resultText.value
                return
            }
            // concatenate
            _equationText.value = it+btn

            // calculate results
            Log.i("Equation",_equationText.value.toString())
        }

    }
}