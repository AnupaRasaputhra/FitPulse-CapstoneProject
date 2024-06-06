package com.example.fitpulse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _bmi = MutableLiveData<Double>()
    val bmi: LiveData<Double> = _bmi

    fun updateBmi(newBmi: Double) {
        _bmi.value = newBmi
    }
}
