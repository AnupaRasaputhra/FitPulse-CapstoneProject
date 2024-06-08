package com.example.fitpulse.ui.setgoals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetGoalsViewModel: ViewModel() {

    private val _selectedGoals = MutableLiveData<String>()
    val selectedGoals:LiveData<String> get() = _selectedGoals

    fun setSelectedGoals(goal: String){
        _selectedGoals.value = goal
    }

}