package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

        setupObservers()
        setupNumberButtons()
        setupOperatorButtons()
        setupActionButtons()
    }

    private fun setupObservers() {
        viewModel.result.observe(this) { result ->
            binding.tvResult.text = result
        }

        viewModel.previousExpression.observe(this) { expression ->
            binding.tvPreviousExpression.text = expression
        }
    }

    private fun setupNumberButtons() {
        binding.btnZero.setOnClickListener { viewModel.addNumber("0") }
        binding.btnOne.setOnClickListener { viewModel.addNumber("1") }
        binding.btnTwo.setOnClickListener { viewModel.addNumber("2") }
        binding.btnThree.setOnClickListener { viewModel.addNumber("3") }
        binding.btnFour.setOnClickListener { viewModel.addNumber("4") }
        binding.btnFive.setOnClickListener { viewModel.addNumber("5") }
        binding.btnSix.setOnClickListener { viewModel.addNumber("6") }
        binding.btnSeven.setOnClickListener { viewModel.addNumber("7") }
        binding.btnEight.setOnClickListener { viewModel.addNumber("8") }
        binding.btnNine.setOnClickListener { viewModel.addNumber("9") }
        binding.btnDot.setOnClickListener { viewModel.addNumber(".") }
    }

    private fun setupOperatorButtons() {
        binding.btnPlus.setOnClickListener { viewModel.setOperator("+") }
        binding.btnMinus.setOnClickListener { viewModel.setOperator("-") }
        binding.btnMultiplication.setOnClickListener { viewModel.setOperator("*") }
        binding.btnDivision.setOnClickListener { viewModel.setOperator("/") }
        binding.btnModulus.setOnClickListener { viewModel.setOperator("%") }
    }

    private fun setupActionButtons() {
        binding.btnEqual.setOnClickListener { viewModel.calculate() }
        binding.tvClear.setOnClickListener { viewModel.clear() }
        binding.btnChangeSign.setOnClickListener { viewModel.toggleSign() }
        binding.ibBack.setOnClickListener { viewModel.backspace() }
    }
}