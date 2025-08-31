package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    private var currentInput = ""
    private var expression = mutableListOf<String>()

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _previousExpression = MutableLiveData<String>()
    val previousExpression: LiveData<String> = _previousExpression

    init {
        _result.value = "0"
        _previousExpression.value = ""
    }

    fun addNumber(number: String) {
        if (number == "." && currentInput.contains(".")) return

        currentInput += number
        updateDisplay()
    }

    fun setOperator(op: String) {
        if (currentInput.isEmpty() && expression.isEmpty()) return

        if (currentInput.isNotEmpty())
            expression.add(currentInput)
        currentInput = ""

        if (expression.isNotEmpty() && isOperator(expression.last()))
            expression[expression.lastIndex] = op
        else expression.add(op)


        updateDisplay()
    }

    fun calculate() {
        if (currentInput.isNotEmpty()) expression.add(currentInput)

        if (expression.isEmpty()) return

        runCatching {
            val result = evaluateExpression(expression.toList())
            _result.value = result.toString()
            _previousExpression.value = "${expression.joinToString(" ")} ="

            currentInput = _result.value.toString()
            expression.clear()
        }.onFailure {
            _result.value = "Error"
            clear()
        }
    }

    fun toggleSign() {
        if (currentInput.isEmpty() || currentInput == "0") return

        currentInput =
            if (currentInput.startsWith("-")) currentInput.drop(1) else "-$currentInput"

        updateDisplay()
    }

    fun clear() {
        currentInput = ""
        expression.clear()
        _result.value = "0"
        _previousExpression.value = ""
    }

    fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        _result.value = when {
            currentInput.isNotEmpty() -> buildDisplayString() + currentInput
            expression.isNotEmpty() -> buildDisplayString()
            else -> "0"
        }
    }

    private fun buildDisplayString() =
        if (expression.isNotEmpty()) expression.joinToString(" ") + " " else ""


    private fun isOperator(token: String) = token in listOf("+", "-", "x", "/", "%")

    private fun evaluateExpression(tokens: List<String>): Double {
        if (tokens.isEmpty()) return 0.0

        val numbers = tokens.filterIndexed { index, _ -> index % 2 == 0 }.map { it.toDouble() }
            .toMutableList()
        val operators = tokens.filterIndexed { index, _ -> index % 2 == 1 }.toMutableList()

        processHighPriorityOperations(numbers, operators)
        return processLowPriorityOperations(numbers, operators)
    }

    private fun processHighPriorityOperations(
        numbers: MutableList<Double>,
        operators: MutableList<String>
    ) {
        var opIndex = 0
        while (opIndex < operators.size) {
            when (operators[opIndex]) {
                Operator.MULTIPLY.operatorStr -> {
                    performOperation(numbers, operators, opIndex) { firstNumber, secondNumber ->
                        firstNumber * secondNumber
                    }
                }

                Operator.DIVIDE.operatorStr -> {
                    performOperation(numbers, operators, opIndex) { firstNumber, secondNumber ->
                        if (secondNumber == 0.0) throw ArithmeticException("Division by zero")
                        firstNumber / secondNumber
                    }
                }

                Operator.MODULUS.operatorStr -> {
                    performOperation(numbers, operators, opIndex) { firstNumber,  secondNumber->
                        if (secondNumber == 0.0) throw ArithmeticException("Division by zero")
                        firstNumber % secondNumber
                    }
                }

                else -> opIndex++
            }
        }
    }

    private fun processLowPriorityOperations(
        numbers: MutableList<Double>,
        operators: MutableList<String>
    ): Double {
        var result = numbers[0]
        for (index in operators.indices) {
            when (operators[index]) {
                Operator.PLUS.operatorStr -> result += numbers[index + 1]
                Operator.MINUS.operatorStr -> result -= numbers[index + 1]
            }
        }
        return result
    }

    private fun performOperation(
        numbers: MutableList<Double>,
        operators: MutableList<String>,
        opIndex: Int,
        operation: (Double, Double) -> Double
    ) {
        val result = operation(numbers[opIndex], numbers[opIndex + 1])
        numbers[opIndex] = result
        numbers.removeAt(opIndex + 1)
        operators.removeAt(opIndex)
    }
}