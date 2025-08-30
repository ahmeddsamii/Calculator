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
        binding.btnZero.setOnClickListener { viewModel.addNumber(getString(R.string._0)) }
        binding.btnOne.setOnClickListener { viewModel.addNumber(getString(R.string._1)) }
        binding.btnTwo.setOnClickListener { viewModel.addNumber(getString(R.string._2)) }
        binding.btnThree.setOnClickListener { viewModel.addNumber(getString(R.string._3)) }
        binding.btnFour.setOnClickListener { viewModel.addNumber(getString(R.string._4)) }
        binding.btnFive.setOnClickListener { viewModel.addNumber(getString(R.string._5)) }
        binding.btnSix.setOnClickListener { viewModel.addNumber(getString(R.string._6)) }
        binding.btnSeven.setOnClickListener { viewModel.addNumber(getString(R.string._7)) }
        binding.btnEight.setOnClickListener { viewModel.addNumber(getString(R.string._8)) }
        binding.btnNine.setOnClickListener { viewModel.addNumber(getString(R.string._9)) }
        binding.btnDot.setOnClickListener { viewModel.addNumber(getString(R.string.dot)) }
    }

    private fun setupOperatorButtons() {
        binding.btnPlus.setOnClickListener { viewModel.setOperator(getString(R.string.plus)) }
        binding.btnMinus.setOnClickListener { viewModel.setOperator(getString(R.string.minus)) }
        binding.btnMultiplication.setOnClickListener { viewModel.setOperator(getString(R.string.x_operator)) }
        binding.btnDivision.setOnClickListener { viewModel.setOperator(getString(R.string.division)) }
        binding.btnModulus.setOnClickListener { viewModel.setOperator(getString(R.string.modulus)) }
    }

    private fun setupActionButtons() {
        binding.btnEqual.setOnClickListener { viewModel.calculate() }
        binding.tvClear.setOnClickListener { viewModel.clear() }
        binding.btnChangeSign.setOnClickListener { viewModel.toggleSign() }
        binding.ibBack.setOnClickListener { viewModel.backspace() }
    }
}