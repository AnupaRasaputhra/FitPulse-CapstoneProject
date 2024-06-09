package com.example.fitpulse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentHomeBinding
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel

class HomeFragment : Fragment() {

    private val viewModel: SetGoalsViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedStepsGoal.observe(viewLifecycleOwner) { stepsGoal ->
            binding.stepTrackerCardView.findViewById<TextView>(R.id.targetSteps).text = "/${stepsGoal} Steps"
        }

        viewModel.selectedWaterGoal.observe(viewLifecycleOwner) { waterGoal ->
            binding.waterIntakeCardView.findViewById<TextView>(R.id.waterTarget).text = "/${waterGoal} Ltr"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}