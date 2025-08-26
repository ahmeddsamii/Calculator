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
        _result.value = currentNumber
    }

    fun setOperator(op: String) {
        if (currentNumber.isEmpty()) return
        firstNumber = currentNumber
        operator = op
        val previousExpression = "$firstNumber $op"
        _previousExpression.value = previousExpression
        currentNumber = ""
    }

    fun calculate() {
        if (firstNumber.isEmpty() || currentNumber.isEmpty() || operator.isEmpty()) return

        val num1 = firstNumber.toDouble()
        val num2 = currentNumber.toDouble()

        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> if (num2 != 0.0) num1 / num2 else 0
            "%" -> if (num2 != 0.0) num1 % num2 else 0
            else -> 0.0
        }

        _result.value = result.toString()

        val previousExpression = "$firstNumber $operator $currentNumber ="
        _previousExpression.value = previousExpression
        currentNumber = _result.value.toString()
        firstNumber = ""
        operator = ""
    }

    fun toggleSign() {
        if (currentNumber.isEmpty()) return

        currentNumber =
            if (currentNumber.startsWith("-")) currentNumber.drop(1) else "-$currentNumber"

        _result.value = currentNumber
    }

    fun clear() {
        currentNumber = ""
        firstNumber = ""
        operator = ""
        _result.value = "0"
        _previousExpression.value = ""
    }

    fun backspace() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
            _result.value = currentNumber.ifEmpty { "0" }
        }
    }
}