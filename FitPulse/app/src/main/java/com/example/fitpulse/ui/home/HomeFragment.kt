package com.example.fitpulse.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentHomeBinding
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel

class HomeFragment() : Fragment(), SensorEventListener {

    private val viewModel: SetGoalsViewModel by activityViewModels()
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

        viewModel.selectedStepsGoal.observe(viewLifecycleOwner) { stepsGoal ->
            binding.stepTrackerCardView.findViewById<TextView>(R.id.targetSteps).text =
                "/${stepsGoal}"
            binding.circularProgressBar.progressMax = stepsGoal.toFloat()
        }

        viewModel.selectedWaterGoal.observe(viewLifecycleOwner) { waterGoal ->
            binding.waterIntakeCardView.findViewById<TextView>(R.id.waterTarget).text =
                "${waterGoal}"
        }

        binding.waterIntakeCardView.setOnClickListener {
            val waterGoal = binding.waterIntakeCardView.findViewById<TextView>(R.id.waterTarget).text.toString().replace("/", "").toInt()
            val waterIntake = binding.waterIntake.findViewById<TextView>(R.id.waterIntake).text.toString().toInt()

            val bundle = Bundle()
            bundle.putInt("waterGoal", waterGoal)
            bundle.putInt("waterIntake", waterIntake)

            findNavController().navigate(R.id.action_homeFragment_to_waterIntakeFragment, bundle)
        }

        binding.logWorkoutCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_logWorkoutFragment)
        }

        val waterIntake = arguments?.getInt("waterIntake", 0)
        updateWaterIntakeTextView(waterIntake ?: 0)
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
            Toast.makeText(requireContext(), "No motion has been detected.", Toast.LENGTH_SHORT).show()
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

            binding.circularProgressBar.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }

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

    private fun loadData(){
        val sharedPreferences = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedSteps = sharedPreferences?.getFloat("Key 1", 0f)
        previousStep = savedSteps!!

    }
}
