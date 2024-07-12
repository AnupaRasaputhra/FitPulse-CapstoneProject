package com.example.fitpulse.ui.logWorkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitpulse.databinding.FragmentLogWorkoutBinding

class LogWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentLogWorkoutBinding
    private lateinit var logViewModel: LogWorkoutViewModel
    private lateinit var workoutHistoryAdapter: WorkoutHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logViewModel = ViewModelProvider(this).get(LogWorkoutViewModel::class.java)
        workoutHistoryAdapter = WorkoutHistoryAdapter(requireContext(), logViewModel.workoutHistory.value ?: mutableListOf())

        binding.buttonSaveWorkout.setOnClickListener {
            val title = binding.workoutTitle.text.toString().trim()
            val description = binding.workoutDescription.text.toString().trim()
            val duration = binding.workoutDuration.text.toString().toIntOrNull() ?: 0

            if (title.isNotEmpty() && duration > 0) {
                val newWorkout = Workout(0, title, duration, description)
                logViewModel.addWorkout(newWorkout)
                workoutHistoryAdapter.notifyDataSetChanged() // Notify adapter of data change
            }
        }

        binding.rvLayout.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutHistoryAdapter
        }

        logViewModel.workoutHistory.observe(viewLifecycleOwner) { workouts ->
            workouts?.let {
                workoutHistoryAdapter.history = it
                workoutHistoryAdapter.notifyDataSetChanged()
            }
        }
    }
}
