package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private var currentNumber = ""
    private var operator = ""
    private var firstNumber = ""

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _previousExpression = MutableLiveData<String>()
    val previousExpression: LiveData<String> = _previousExpression

    init {
        _result.value = "0"
        _previousExpression.value = ""
    }

    fun addNumber(number: String) {
        if (number == "." && currentNumber.contains(".")) return

        currentNumber += number
        updateDisplay()
    }

    fun setOperator(op: String) {
        if (currentNumber.isEmpty()) return

        firstNumber = currentNumber
        operator = op
        currentNumber = ""
        updateDisplay()
    }

    fun calculate() {
        if (!validToCalculate()) return

        val result = performCalculation()
        _result.value = result.toString()
        _previousExpression.value = "$firstNumber $operator $currentNumber ="

        currentNumber = _result.value.toString()
        firstNumber = ""
        operator = ""
    }

    fun toggleSign() {
        if (currentNumber.isEmpty()) return

        currentNumber =
            if (currentNumber.startsWith("-")) currentNumber.drop(1) else "-$currentNumber"

        updateDisplay()
    }

    fun clear() {
        currentNumber = ""
        firstNumber = ""
        operator = ""
        _result.value = "0"
        _previousExpression.value = ""
    }

    fun backspace() {
        if (currentNumber.isNotEmpty()) currentNumber = currentNumber.dropLast(1)

        updateDisplay()
    }

    private fun updateDisplay() {
        _result.value = when {
            operator.isNotEmpty() && firstNumber.isNotEmpty() -> "$firstNumber $operator $currentNumber"
            currentNumber.isNotEmpty() -> currentNumber
            else -> "0"
        }
    }

    private fun validToCalculate() =
        firstNumber.isNotEmpty() && currentNumber.isNotEmpty() && operator.isNotEmpty()


    private fun performCalculation(): Double {
        val num1 = firstNumber.toDouble()
        val num2 = currentNumber.toDouble()

        return when (operator) {
            Operator.PLUS.operatorStr -> num1 + num2
            Operator.MINUS.operatorStr -> num1 - num2
            Operator.MULTIPLY.operatorStr -> num1 * num2
            Operator.DIVIDE.operatorStr -> if (num2 != 0.0) num1 / num2 else 0.0
            Operator.MODULUS.operatorStr -> if (num2 != 0.0) num1 % num2 else 0.0
            else -> 0.0
        }
    }
}