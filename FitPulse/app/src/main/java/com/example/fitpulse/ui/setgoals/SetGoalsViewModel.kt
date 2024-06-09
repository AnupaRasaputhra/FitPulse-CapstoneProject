package com.example.fitpulse.ui.setgoals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetGoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("fitpulse_prefs", 0)

    private val _selectedStepsGoal = MutableLiveData<String>().apply {
        value = sharedPreferences.getString("steps_goal", "0")
    }
    val selectedStepsGoal: LiveData<String> get() = _selectedStepsGoal

    private val _selectedWaterGoal = MutableLiveData<String>().apply {
        value = sharedPreferences.getString("water_goal", "0L")
    }
    val selectedWaterGoal: LiveData<String> get() = _selectedWaterGoal

    fun setSelectedStepsGoal(goal: String) {
        _selectedStepsGoal.value = goal
        saveGoalToPreferences("steps_goal", goal)
    }

    fun setSelectedWaterGoal(goal: String) {
        _selectedWaterGoal.value = goal
        saveGoalToPreferences("water_goal", goal)
    }

    private fun saveGoalToPreferences(key: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferences.edit().putString(key, value).apply()
        }
    }
}