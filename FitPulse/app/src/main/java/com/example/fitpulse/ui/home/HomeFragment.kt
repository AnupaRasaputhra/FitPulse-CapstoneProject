package com.example.fitpulse.ui.home

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.fitpulse.MainActivity
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentHomeBinding
import com.example.fitpulse.ui.footstepsHistory.FootStepsHistoryViewModel
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel
import com.example.fitpulse.ui.inapptutorial.InapptutActivity
import com.example.fitpulse.ui.stepcounter.StepCounterViewModel
import com.example.fitpulse.ui.waterTracker.WaterIntakeViewModel
import java.time.LocalDate

class HomeFragment : Fragment(), SensorEventListener {

    private val viewModel: SetGoalsViewModel by activityViewModels()
    private val sharedViewModel: FootStepsHistoryViewModel by activityViewModels()
    private val stepCounterViewModel: StepCounterViewModel by activityViewModels()
    private val waterIntakeViewModel: WaterIntakeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var stepCounter: Sensor? = null
    private var running = false
    private var totalSteps = 0f
    private var previousStep = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        loadData()
        resetData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            val intent = Intent(requireActivity(), InapptutActivity::class.java)
            startActivity(intent)
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }

        viewModel.selectedStepsGoal.observe(viewLifecycleOwner) { stepsGoal ->
            binding.stepTrackerCardView.findViewById<TextView>(R.id.targetSteps).text = "/${stepsGoal}"
            binding.circularProgressBar.progressMax = stepsGoal.toFloat()
        }

        viewModel.selectedWaterGoal.observe(viewLifecycleOwner) { waterGoal ->
            binding.waterIntakeCardView.findViewById<TextView>(R.id.waterTarget).text =
                "${waterGoal}"
        }

        // Observe water intake data
        waterIntakeViewModel.currentIntake.observe(viewLifecycleOwner) { waterIntake ->
            updateWaterIntakeTextView(waterIntake)
        }

        binding.waterIntakeCardView.setOnClickListener {
            val waterGoal =
                binding.waterIntakeCardView.findViewById<TextView>(R.id.waterTarget).text.toString()
                    .replace("/", "").toInt()
            val waterIntake =
                binding.waterIntake.findViewById<TextView>(R.id.waterIntake).text.toString().toInt()

            val bundle = Bundle()
            bundle.putInt("waterGoal", waterGoal)
            bundle.putInt("waterIntake", waterIntake)

            findNavController().navigate(R.id.action_homeFragment_to_waterIntakeFragment, bundle)
        }

        binding.logWorkoutCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_logWorkoutFragment)
        }

        binding.bmiData.setOnClickListener {
            findNavController().navigate(R.id.nav_bmi_calculator)
        }

        binding.stepTrackerCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_footStepsHistoryFragment)
        }

        // Initialize water intake display
        val waterIntake = waterIntakeViewModel.currentIntake.value ?: 0
        updateWaterIntakeTextView(waterIntake)

        // Set up listener for the water intake result from WaterIntakeFragment
        setFragmentResultListener("waterIntakeKey") { _, bundle ->
            val newWaterIntake = bundle.getInt("waterIntake")
            waterIntakeViewModel.setCurrentIntake(newWaterIntake)
            updateWaterIntakeTextView(newWaterIntake)
        }

    }

    private fun updateWaterIntakeTextView(intake: Int) {
        val waterIntakeTextView = view?.findViewById<TextView>(R.id.waterIntake)
        waterIntakeTextView?.text = intake.toString()
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "No motion has been detected.", Toast.LENGTH_SHORT)
                .show()
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousStep.toInt()
            binding.stepTrackerCardView.findViewById<TextView>(R.id.liveTrackSteps).text =
                "${currentSteps}"
            stepCounterViewModel.updateCurrentSteps(currentSteps)

            binding.circularProgressBar.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }

            val stepsGoal = viewModel.selectedStepsGoal.value!!.toInt()
            if (currentSteps >= stepsGoal) {
                (activity as MainActivity).pushNotification()
            }

            val dayOfWeek = LocalDate.now().dayOfWeek.toString()
            sharedViewModel.updateStepsData(dayOfWeek, currentSteps)
            stepCounterViewModel.updateCurrentSteps(currentSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun resetData() {
        binding.liveTrackSteps.setOnLongClickListener {
            previousStep = totalSteps
            binding.liveTrackSteps.text = 0.toString()
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putFloat("Key 1", previousStep)
        editor?.apply()
    }

    private fun loadData() {
        val sharedPreferences = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedSteps = sharedPreferences?.getFloat("Key 1", 0f)
        previousStep = savedSteps!!

    }
}