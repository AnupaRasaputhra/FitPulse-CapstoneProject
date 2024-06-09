package com.example.fitpulse.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentSlideshowBinding
import com.example.fitpulse.ui.home.HomeViewModel

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var slideshowViewModel: SlideshowViewModel
    var callback: OnSubmitClickListener? = null
    private lateinit var homeViewModel: HomeViewModel

    interface OnSubmitClickListener {
        fun onSubmitClicked(calculatedCalories: Double)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSubmitClickListener) {
            callback = context
        } else {
            throw RuntimeException("context doesn't implement OnSubmitClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        slideshowViewModel = ViewModelProvider(this)[SlideshowViewModel::class.java]

        val textView: TextView = binding.textView2
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Observe LiveData properties and update UI
        slideshowViewModel.age.observe(viewLifecycleOwner) { newAge ->
            if (binding.textInputAge.text.toString() != newAge) {
                binding.textInputAge.setText(newAge)
            }
        }

        slideshowViewModel.height.observe(viewLifecycleOwner) { newHeight ->
            val currentHeightText = binding.textInputHeight.text.toString()
            if (currentHeightText != newHeight.toString()) {
                val cursorPosition = binding.textInputHeight.selectionStart
                binding.textInputHeight.setText(newHeight.toString())
                binding.textInputHeight.setSelection(cursorPosition)
            }
        }

        slideshowViewModel.weight.observe(viewLifecycleOwner) { newWeight ->
            val currentWeightText = binding.textInputWeight.text.toString()
            if (currentWeightText != newWeight.toString()) {
                val cursorPosition = binding.textInputWeight.selectionStart
                binding.textInputWeight.setText(newWeight.toString())
                binding.textInputWeight.setSelection(cursorPosition)
            }
        }

        slideshowViewModel.gender.observe(viewLifecycleOwner) { newGender ->
            val selectedGender = when (newGender) {
                "Male" -> R.id.radioMale
                "Female" -> R.id.radioFemale
                else -> -1
            }
            if (selectedGender != -1 && binding.radioGroupGender.checkedRadioButtonId != selectedGender) {
                binding.radioGroupGender.check(selectedGender)
            }
        }

        // Set up the RadioGroup for gender
        binding.radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val gender = when (checkedId) {
                R.id.radioMale -> "Male"
                R.id.radioFemale -> "Female"
                else -> ""
            }
            if (slideshowViewModel.gender.value != gender) {
                slideshowViewModel.updateGender(gender)
            }
        }

        // Add text change listeners to update the ViewModel
        binding.textInputAge.addTextChangedListener { text ->
            if (slideshowViewModel.age.value != text.toString()) {
                slideshowViewModel.updateAge(text.toString())
            }
        }

        binding.textInputHeight.addTextChangedListener { text ->
            try {
                slideshowViewModel.updateHeight(text.toString().toDouble())
            } catch (e: NumberFormatException) {
                Log.e("Exception", "cannot update Height")
            }
        }

        binding.textInputWeight.addTextChangedListener { text ->
            try {
                slideshowViewModel.updateWeight(text.toString().toDouble())
            } catch (e: NumberFormatException) {
                Log.e("Exception", "cannot update Weight")
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val age = binding.textInputAge.text.toString()
            val height = binding.textInputHeight.text.toString()
            val weight = binding.textInputWeight.text.toString()
            val gender = when (binding.radioGroupGender.checkedRadioButtonId) {
                R.id.radioMale -> "Male"
                R.id.radioFemale -> "Female"
                else -> ""
            }

            slideshowViewModel.updateAge(age)
            slideshowViewModel.updateHeight(height.toDouble())
            slideshowViewModel.updateWeight(weight.toDouble())
            slideshowViewModel.updateGender(gender)
            slideshowViewModel.calculateCaloriesNeeds()
            slideshowViewModel.caloriesNeeded.value?.let { calculatedCalories ->
                callback?.onSubmitClicked(calculatedCalories)
                val formattedResult = "Your estimated daily caloric need is:\n" + "$calculatedCalories calories"
                binding.resultTV.text = formattedResult
                binding.resultTV.visibility = View.VISIBLE
            }
            // Hide the keyboard
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // Observe LiveData properties and update UI
        slideshowViewModel.caloriesNeeded.observe(viewLifecycleOwner) { caloriesValue ->
            // Update BMI value in HomeViewModel
            homeViewModel.updateCaloriesNeeded(caloriesValue)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
