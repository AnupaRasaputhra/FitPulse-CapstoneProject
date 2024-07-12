package com.example.fitpulse.ui.waterTracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fitpulse.MainActivity
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentWaterIntakeBinding
import com.google.android.material.textfield.TextInputLayout

class WaterIntakeFragment : Fragment() {
    private var _binding: FragmentWaterIntakeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WaterIntakeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWaterIntakeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WaterIntakeViewModel::class.java)
        observeViewModel()
        setupClickListeners()

        val waterGoal = arguments?.getInt("waterGoal", 0) ?: 0
        val waterIntake = arguments?.getInt("waterIntake", 0) ?: 0

        viewModel.setTargetIntake(waterGoal)
        viewModel.setCurrentIntake(waterIntake)

        // Set the app bar title
        (requireActivity() as MainActivity).setActionBarTitle("Water Intake")
    }






    private fun observeViewModel() {
        viewModel.targetIntake.observe(viewLifecycleOwner, { targetIntake ->
            binding.tvTotalIntake.text = targetIntake.toString()
        })

        viewModel.currentIntake.observe(viewLifecycleOwner, { currentIntake ->
            binding.tvIntook.text = currentIntake.toString()
        })


        viewModel.goalAchieved.observe(viewLifecycleOwner, { goalAchieved ->
            if (goalAchieved) {
                showCongratulatoryAlert()
            }
        })
    }

    private fun setupClickListeners() {
        binding.op50ml.setOnClickListener { addWaterIntake(50) }
        binding.op100ml.setOnClickListener { addWaterIntake(100) }
        binding.op150ml.setOnClickListener { addWaterIntake(150) }
        binding.op200ml.setOnClickListener { addWaterIntake(200) }
        binding.op250ml.setOnClickListener { addWaterIntake(250) }

        binding.opCustom.setOnClickListener {
            showCustomInputDialog()
        }
    }

    private fun addWaterIntake(amount: Int) {
        viewModel.addToCurrentIntake(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()

        val currentIntake = binding.tvIntook.text.toString().toInt()
        val bundle = Bundle().apply {
            putInt("waterIntake", currentIntake)
        }
        findNavController().navigate(R.id.action_waterIntakeFragment_to_homeFragment, bundle)
    }

    private fun showCustomInputDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val promptsView = inflater.inflate(R.layout.custom_input_dialog, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(promptsView)

        val userInput = promptsView.findViewById<TextInputLayout>(R.id.etCustomInput)

        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            val inputText = userInput.editText?.text.toString()
            if (inputText.isEmpty()) {
                Toast.makeText(requireContext(), "Input cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val inputValue = inputText.toInt()
                val currentIntake = viewModel.currentIntake.value ?: 0
                if (currentIntake + inputValue < 0) {
                    Toast.makeText(requireContext(), "Intake cannot be less than 0", Toast.LENGTH_SHORT).show()
                } else {
                    binding.tvIntook.text = "${inputText} ml"
                    viewModel.addToCurrentIntake(inputValue)
                }
                dialog.dismiss()
            }
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun showCongratulatoryAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage("You've reached your water intake goal!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

}