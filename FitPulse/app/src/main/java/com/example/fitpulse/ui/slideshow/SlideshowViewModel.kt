package com.example.fitpulse.ui.slideshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitpulse.ui.home.HomeViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Why calories are important. You need energy from calories for your body to work properly. Your body uses this energy to function properly. Calculate the amount of calories you need "
    }
    private val _age = MutableLiveData<String>()
    private val _height = MutableLiveData<Double>()
    private val _weight = MutableLiveData<Double>()
    private val _gender = MutableLiveData<String>()
    private val _bmi = MutableLiveData<Double>()
    val text: LiveData<String> = _text
    val age: LiveData<String> = _age
    val height: LiveData<Double> = _height
    val weight: LiveData<Double> = _weight
    val gender: LiveData<String> = _gender
    val bmi: LiveData<Double> = _bmi
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

    fun calculateBmi() {
        val heightValue = _height.value
        val weightValue = _weight.value
        if (heightValue != null && weightValue != null) {
            _bmi.value = (weightValue.div((heightValue * heightValue))).toDouble()
            Log.e("BMI VALUE: ", "${_bmi.value}")
        }

        Log.e("CALCULATED BMI", "${_bmi.value}")
    }
}