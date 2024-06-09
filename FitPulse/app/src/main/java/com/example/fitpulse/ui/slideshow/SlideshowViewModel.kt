package com.example.fitpulse.ui.slideshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value =
            "Why calories are important. You need energy from calories for your body to work properly. Your body uses this energy to function properly. Calculate the amount of calories you need "
    }
    private val _age = MutableLiveData<String>()
    private val _height = MutableLiveData<Double>()
    private val _weight = MutableLiveData<Double>()
    private val _gender = MutableLiveData<String>()
    private val _caloriesNeeded = MutableLiveData<Double>()
    val text: LiveData<String> = _text
    val age: LiveData<String> = _age
    val height: LiveData<Double> = _height
    val weight: LiveData<Double> = _weight
    val gender: LiveData<String> = _gender
    val caloriesNeeded: LiveData<Double> = _caloriesNeeded
    fun updateAge(newAge: String) {
        _age.value = newAge
    }

    fun updateHeight(newHeight: Double) {
        _height.value = newHeight
    }

    fun updateWeight(newWeight: Double) {
        _weight.value = newWeight
    }

    fun updateGender(newGender: String) {
        _gender.value = newGender
    }

    fun calculateCaloriesNeeds() {
        val age = _age.value?.toIntOrNull()
        val height = _height.value
        val weight = _weight.value
        val gender = _gender.value

        if (age != null && height != null && weight != null && !gender.isNullOrEmpty()) {
            val calculatedCalories = when (gender) {
                "Male" -> 10 * weight + 6.25 * height - 5 * age + 5
                "Female" -> 10 * weight + 6.25 * height - 5 * age - 161
                else -> 0.0
            }
            _caloriesNeeded.value = calculatedCalories
        }
    }
}