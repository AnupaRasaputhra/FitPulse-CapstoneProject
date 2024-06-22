package com.example.fitpulse.ui.setgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fitpulse.R
import com.example.fitpulse.databinding.DialogSetGoalsBinding

class SetGoalsDialogFragment : DialogFragment() {

    private val viewModel: SetGoalsViewModel by activityViewModels()
    private var _binding: DialogSetGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSetGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goalOptions = arrayOf("Foot Steps", "Water Intake")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, goalOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.goalTypeSpinner.adapter = adapter

        binding.setGoalButton.setOnClickListener {
            val selectedGoalType = binding.goalTypeSpinner.selectedItem.toString()
            val goalValue = binding.goalValueEditText.text.toString()

            if (selectedGoalType == "Foot Steps") {
                viewModel.setSelectedStepsGoal(goalValue)
            } else {
                viewModel.setSelectedWaterGoal(goalValue)
            }
            
            dismiss()
            findNavController().navigate(R.id.nav_home)
        }
    }

    private fun navigateToHome() {
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}