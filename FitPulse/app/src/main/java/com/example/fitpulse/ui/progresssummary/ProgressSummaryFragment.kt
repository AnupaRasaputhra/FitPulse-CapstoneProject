package com.example.fitpulse.ui.progresssummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitpulse.databinding.FragmentProgressSummaryBinding
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel
import com.example.fitpulse.ui.waterTracker.WaterIntakeViewModel
import com.example.fitpulse.ui.stepcounter.StepCounterViewModel

class ProgressSummaryFragment : Fragment() {

    private var _binding: FragmentProgressSummaryBinding? = null
    private val binding get() = _binding!!

    private val setGoalsViewModel: SetGoalsViewModel by activityViewModels()
    private val waterIntakeViewModel: WaterIntakeViewModel by activityViewModels()
    private val stepCounterViewModel: StepCounterViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStepProgress()
        observeWaterIntake()
    }

    private fun observeStepProgress() {
        setGoalsViewModel.selectedStepsGoal.observe(viewLifecycleOwner) { stepsGoal ->
            updateStepsProgressChart(stepsGoal.toIntOrNull() ?: 10000, stepCounterViewModel.currentSteps.value ?: 0)
        }

        stepCounterViewModel.currentSteps.observe(viewLifecycleOwner) { currentSteps ->
            updateStepsProgressChart(setGoalsViewModel.selectedStepsGoal.value?.toIntOrNull() ?: 10000, currentSteps)
        }
    }

    private fun observeWaterIntake() {
        waterIntakeViewModel.currentIntake.observe(viewLifecycleOwner) { currentIntake ->
            val waterGoal = setGoalsViewModel.selectedWaterGoal.value?.toIntOrNull() ?: 2000
            updateWaterIntakeChart(waterGoal, currentIntake)
        }
    }

    private fun updateStepsProgressChart(stepsGoal: Int, currentSteps: Int = 0) {
        val progress = ((currentSteps.toFloat() / stepsGoal.toFloat()) * 100).toInt()

        binding.stepsProgressBar.apply {
            max = 100
            setProgress(progress, true)
        }

        binding.stepsProgressText.text = "${currentSteps}/${stepsGoal} steps"
    }

    private fun updateWaterIntakeChart(waterGoal: Int, currentIntake: Int) {
        val progress = ((currentIntake.toFloat() / waterGoal.toFloat()) * 100).toInt()

        binding.waterIntakeProgressBar.apply {
            max = 100
            setProgress(progress, true)
        }

        binding.waterIntakeProgressText.text = "${currentIntake}/${waterGoal} ml"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}