package com.example.fitpulse.ui.logWorkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogWorkoutViewModel : ViewModel() {
    private val _workoutHistory = MutableLiveData<MutableList<Workout>>()
    val workoutHistory: LiveData<MutableList<Workout>>
        get() = _workoutHistory

    init {
        _workoutHistory.value = mutableListOf()
    }

    fun addWorkout(workout: Workout) {
        val currentList = _workoutHistory.value ?: mutableListOf()
        currentList.add(workout)
        _workoutHistory.value = currentList
    }
}
