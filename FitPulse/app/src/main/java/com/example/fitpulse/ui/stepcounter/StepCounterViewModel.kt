package com.example.fitpulse.ui.stepcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepCounterViewModel : ViewModel() {
    private val _currentSteps = MutableLiveData<Int>()
    val currentSteps: LiveData<Int> = _currentSteps

    fun updateCurrentSteps(steps: Int) {
        _currentSteps.value = steps
    }
}