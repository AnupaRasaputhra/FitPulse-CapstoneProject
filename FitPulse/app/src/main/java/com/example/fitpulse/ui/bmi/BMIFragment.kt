package com.example.fitpulse.ui.bmi

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fitpulse.databinding.BmiFragmentBinding

class BMIFragment : Fragment() {
    private lateinit var bmiBinding: BmiFragmentBinding
    private lateinit var bmiViewModel: BMIViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bmiBinding = BmiFragmentBinding.inflate(inflater, container, false)
        val view = bmiBinding.root

        bmiViewModel = ViewModelProvider(this).get(BMIViewModel::class.java)

        bmiBinding.buttonCalculate.setOnClickListener {
            val weight = bmiBinding.inputWeight.text.toString().toFloatOrNull()
            val height = bmiBinding.inputHeight.text.toString().toFloatOrNull()

            if (weight != null && height != null) {
                val bmi = bmiViewModel.calculateBMI(weight, height)
                val (bodyLevel, color) = bmiViewModel.determineBodyLevel(bmi)
                displayResult(bmi, bodyLevel, color)
            } else {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        sharedPref = requireActivity().getSharedPreferences("sf", MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()

        loadSavedData()

        return view
    }

    override fun onPause() {
        super.onPause()

        val weight = bmiBinding.inputWeight.text.toString()
        val height = bmiBinding.inputHeight.text.toString()

        sharedPrefEditor.apply {
            putInt("weight_sf", weight.toInt())
            putInt("height_sf", height.toInt())
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedData()
    }

    private fun loadSavedData() {
        val weightValue = sharedPref.getInt("weight_sf", 0)
        val heightValue = sharedPref.getInt("height_sf", 0)

        if (weightValue != 0 && heightValue != 0) {
            bmiBinding.inputWeight.setText(weightValue.toString())
            bmiBinding.inputHeight.setText(heightValue.toString())
        }
    }

    private fun displayResult(bmi: Float, bodyLevel: String, color: Int) {
        bmiBinding.textIndex.text = bmi.toString()
        bmiBinding.textResult.text = bodyLevel
        bmiBinding.textResult.setTextColor(resources.getColor(color, null))
    }
}
