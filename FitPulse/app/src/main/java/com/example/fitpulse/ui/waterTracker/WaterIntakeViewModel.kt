package com.example.fitpulse.ui.waterTracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WaterIntakeViewModel : ViewModel() {
    private val _currentIntake = MutableLiveData<Int>(0)
    val currentIntake: LiveData<Int> = _currentIntake

    private val _targetIntake = MutableLiveData<String>()
    val targetIntake: LiveData<String>
        get() = _targetIntake

    private val _goalAchieved = MutableLiveData<Boolean>(false)
    val goalAchieved: LiveData<Boolean> = _goalAchieved

    private var waterGoal: Int = 0
    private var alertShown: Boolean = false

    fun addToCurrentIntake(amount: Int) {
        val current = _currentIntake.value ?: 0
        _currentIntake.value = current + amount
        checkGoalAchieved()
    }

    fun setTargetIntake(waterGoal: Int) {
        this.waterGoal = waterGoal
        _targetIntake.value = "/$waterGoal ml"
        checkGoalAchieved()
    }

    fun setCurrentIntake(intake: Int) {
        _currentIntake.value = intake
        checkGoalAchieved()
    }

    private fun checkGoalAchieved() {
        val current = _currentIntake.value ?: 0
        if (current >= waterGoal && !alertShown) {
            _goalAchieved.value = true
            alertShown = true
        } else {
            _goalAchieved.value = false
        }
    }
}
