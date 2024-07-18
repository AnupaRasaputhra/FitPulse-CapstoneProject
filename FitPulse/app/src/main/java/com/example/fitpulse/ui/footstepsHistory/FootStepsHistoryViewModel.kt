package com.example.fitpulse.ui.footstepsHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FootStepsHistoryViewModel : ViewModel() {

    private val _stepsData = MutableLiveData<Map<String, Int>>()
    val stepsData: LiveData<Map<String, Int>> get() = _stepsData

    fun updateStepsData(day: String, steps: Int) {
        val currentStep = _stepsData.value ?: emptyMap()
        _stepsData.value = currentStep.toMutableMap().apply {
            put(day, steps)
        }
    }
}