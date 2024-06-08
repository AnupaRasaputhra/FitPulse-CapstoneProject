package com.example.fitpulse.ui.bmi

import androidx.lifecycle.ViewModel
import com.example.fitpulse.R

class BMIViewModel : ViewModel() {
    fun calculateBMI(weight: Float, height: Float): Float {
        return weight / ((height / 100) * (height / 100))
    }

    fun determineBodyLevel(bmi: Float): Pair<String, Int> {
        return when {
            bmi < 18.50 -> Pair("Underweight", R.color.under_weight)
            bmi in 18.50..24.99 -> Pair("Healthy", R.color.normal)
            bmi in 25.00..29.90 -> Pair("Overweight", R.color.over_weight)
            else -> Pair("Obese", R.color.obese)
        }
    }
}
