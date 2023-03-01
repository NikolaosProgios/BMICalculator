package com.example.bmicalculator

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var unitUs = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            calculateBtn.setOnClickListener { calculateBMI() }
            swipeUnit.setOnClickListener { swipeUnit() }
            weightEt.setOnEditorActionListener { _, actionId, event ->
                if (event != null || actionId == EditorInfo.IME_ACTION_NEXT && weightEt.text.isNullOrEmpty()) {
                    showToastMessage(R.string.WeightIsEmpty)
                }
                false
            }
            heightEt.setOnEditorActionListener { _, actionId, event ->
                if (event != null || actionId == EditorInfo.IME_ACTION_DONE) {
                    calculateBMI()
                }
                false
            }
            heightEtInch.setOnEditorActionListener { _, actionId, event ->
                if (event != null || actionId == EditorInfo.IME_ACTION_DONE) {
                    calculateBMI()
                }
                false
            }
        }
    }

    private fun calculateBMI() {
        val weight = binding.weightEt.text.toString()
        val height = binding.heightEt.text.toString()
        val heightInch = binding.heightEtInch.text.toString()

        if (validateInput(weight, height, heightInch)) {
            hideKeyboard()
            val bmi = calculateBMI(weight, height, heightInch)
            val bmi2Digits = String.format("%.2f", bmi).replace(',','.').toFloat()
            displayResult(bmi2Digits)
        }
    }

    private fun calculateBMI(weight: String, height: String, heightInch: String): Float {
        return if (!unitUs) {
            weight.toFloat() / height.toFloat() / height.toFloat() * 10000
        } else {
            val height = height.ifEmpty { "0" }
            val heightInch = heightInch.ifEmpty { "0" }
            val heightInches = (height.toDouble() * 12) + heightInch.toDouble()
            (weight.toFloat() / (heightInches.toFloat() * heightInches.toFloat())) * 703
        }
    }

    private fun validateInput(weight: String?, height: String?, heightInch:String?): Boolean {
        return when {
            weight.isNullOrEmpty() && height.isNullOrEmpty() && heightInch.isNullOrEmpty() -> {
                showToastMessage(R.string.WightHighIsEmpty)
                false
            }
            weight.isNullOrEmpty() -> {
                showToastMessage(R.string.WeightIsEmpty)
                false
            }
            height.isNullOrEmpty() && heightInch.isNullOrEmpty() -> {
                showToastMessage(R.string.HighIsEmpty)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun displayResult(bmi: Float) {
        var resultText: String? = null
        var color = 0

        when {
            bmi < 18.50 -> {
                if (bmi < 10) showToastMessage(R.string.CheckYourValues)
                resultText = getString(R.string.Underweight)
                color = R.color.under_weight
            }
            bmi in 18.50..24.99 -> {
                resultText = getString(R.string.Healthy)
                color = R.color.normal
            }
            bmi in 25.00..29.99 -> {
                resultText = getString(R.string.Overweight)
                color = R.color.over_weight
            }
            bmi > 29.99 -> {
                if (bmi > 80) showToastMessage(R.string.CheckYourValues)
                resultText = getString(R.string.Obese)
                color = R.color.obese
            }
        }

        binding.apply {
            infoTv.text = getString(R.string.normal_range_bmi)
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

    private fun clearResultValues() {
        binding.apply {
            weightEt.text.clear()
            heightEt.text.clear()
            heightEtInch.text.clear()
            infoTv.text = ""
            indexTv.text = ""
            resultTv.text = ""
        }
    }

    private fun swipeUnit() {
        if (!unitUs) showUsUnit()
        else showMetricUnit()
    }

    private fun showUsUnit() {
        unitUs = true
        clearResultValues()
        with(binding) {
            weightUnit.text = getString(R.string.pounds)
            heightUnit.text = getString(R.string.feet)
            heightEtInch.isVisible = true
            heightUnitInch.isVisible = true
            heightEt.imeOptions = EditorInfo.IME_ACTION_NEXT
            heightUnit.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun showMetricUnit() {
        unitUs = false
        clearResultValues()
        with(binding) {
            weightUnit.text = getString(R.string.kg)
            heightUnit.text = getString(R.string.cm)
            heightEtInch.isVisible = false
            heightUnitInch.isVisible = false
            heightEt.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private fun showToastMessage(textID: Int) {
        Toast.makeText(this, getString(textID), Toast.LENGTH_SHORT).show()
    }
}