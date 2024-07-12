package com.example.fitpulse.ui.logWorkout

data class Workout(
    val id: Int,
    val title: String,
    val duration: Int,
    val description: String? = null
)