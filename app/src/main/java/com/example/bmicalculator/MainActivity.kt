package com.example.bmicalculator

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateBtn.setOnClickListener {
            calculateBMI()
        }
    }

    private fun calculateBMI() {
        val weight = binding.weightEt.text.toString()
        val height = binding.heightEt.text.toString()
        if (validateInput(weight, height)) {
            val bmi = weight.toFloat() / ((height.toFloat() / 100) * (height.toFloat() / 100))
            // get result with two decimal places
            val bmi2Digits = String.format("%.2f", bmi).toFloat()
            displayResult(bmi2Digits)
        }
        hideKeyboard()
    }

    private fun validateInput(weight: String?, height: String?): Boolean {

        return when {
            weight.isNullOrEmpty() -> {
                Toast.makeText(this, "Weight is empty", Toast.LENGTH_LONG).show()
                false
            }
            height.isNullOrEmpty() -> {
                Toast.makeText(this, "Height is empty", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun displayResult(bmi: Float) {
        val info = findViewById<TextView>(R.id.info_tv)

        info.setText(R.string.normal_range_bmi)

        var resultText = ""
        var color = 0

        when {
            bmi < 18.50 -> {
                resultText = "Underweight"
                color = R.color.under_weight
            }
            bmi in 18.50..24.99 -> {
                resultText = "Healthy"
                color = R.color.normal
            }
            bmi in 25.00..29.99 -> {
                resultText = "Overweight"
                color = R.color.over_weight
            }
            bmi > 29.99 -> {
                resultText = "Obese"
                color = R.color.obese
            }

        }
        binding.apply {
            indexTv.text = bmi.toString()
            resultTv.text = resultText
            resultTv.setTextColor(resources.getColor(color))
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

    }

}