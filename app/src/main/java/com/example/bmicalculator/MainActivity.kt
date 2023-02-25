package com.example.bmicalculator

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
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
            swipeUnit.setOnClickListener {
                if(!unitUs) swipeUnit()
                else showMetricUnit()}
            heightEt.setOnEditorActionListener { v, actionId, event ->
                if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    calculateBMI()
                }
                false
            }
        }
    }

    private fun calculateBMI() {
        val weight = binding.weightEt.text.toString()
        val height = binding.heightEt.text.toString()
        if (validateInput(weight, height)) {
            hideKeyboard()
            val bmi =
                if (!unitUs) (weight.toFloat() / height.toFloat() / height.toFloat() * 10000)
                else (weight.toFloat() / height.toFloat() / height.toFloat() * 10000) * 703
            val bmi2Digits = String.format("%.2f", bmi).toFloat()
            displayResult(bmi2Digits)
        }
    }

    private fun validateInput(weight: String?, height: String?): Boolean {

        return when {
            weight.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.WeightIsEmpty), Toast.LENGTH_LONG).show()
                clearResultValues()
                false
            }
            height.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.HighIsEmpty), Toast.LENGTH_LONG).show()
                clearResultValues()
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
                if (bmi < 10) Toast.makeText(this, getString(R.string.CheckYourValues), Toast.LENGTH_LONG).show()
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
                if (bmi > 80) Toast.makeText(this, getString(R.string.CheckYourValues), Toast.LENGTH_LONG).show()
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
            infoTv.text=""
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
        }
    }
}