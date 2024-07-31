package com.example.fitpulse.ui.inapptutorial

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitpulse.R

class InapptutFragment : Fragment() {

    companion object {
        fun newInstance(layoutResId: Int): InapptutFragment {
            val fragment = InapptutFragment()
            val args = Bundle().apply {
                putInt("LAYOUT_RES_ID", layoutResId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutResId = arguments?.getInt("LAYOUT_RES_ID") ?: R.layout.inapptut_screen1
        val view = inflater.inflate(layoutResId, container, false)
        
        when (layoutResId) {
            R.layout.inapptut_screen1 -> setupWelcomeScreen(view)
            R.layout.inapptut_screen2 -> setupStepTrackingDemo(view)
            R.layout.inapptut_screen3 -> setupWaterIntakeDemo(view)
            R.layout.inapptut_screen4 -> setupBMICalculatorDemo(view)
            R.layout.inapptut_screen5 -> setupGoalSettingDemo(view)
        }
        
        setupButtonClickListener(view, layoutResId)
        
        return view
    }

    private fun setupWelcomeScreen(view: View) {
        val getStartedButton = view.findViewById<Button>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            (activity as? InapptutActivity)?.moveToNextPage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupStepTrackingDemo(view: View) {
        val stepCounterDemo = view.findViewById<TextView>(R.id.stepCounterDemo)
        var steps = 0
        view.setOnClickListener {
            steps++
            stepCounterDemo.text = "Steps: $steps / 10,000"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupWaterIntakeDemo(view: View) {
        val waterIntakeDemo = view.findViewById<TextView>(R.id.waterIntakeDemo)
        var glasses = 0
        view.setOnClickListener {
            glasses++
            waterIntakeDemo.text = "Water Intake: $glasses / 8 glasses"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupBMICalculatorDemo(view: View) {
        val bmiCalculatorDemo = view.findViewById<TextView>(R.id.bmiCalculatorDemo)
        bmiCalculatorDemo.text = "Tap to calculate your BMI"
        view.setOnClickListener {
            bmiCalculatorDemo.text = "Your BMI: 22.5 (Normal)"
        }
    }

    private fun setupGoalSettingDemo(view: View) {
        val goalSettingDemo = view.findViewById<TextView>(R.id.goalSettingDemo)
        view.setOnClickListener {
            goalSettingDemo.text = "Goals set:\nSteps: 10,000\nWater Intake: 8 glasses"
        }
        view.findViewById<Button>(R.id.finishButton)?.setOnClickListener {
            (activity as? InapptutActivity)?.finishTutorial()
        }
    }

    private fun setupButtonClickListener(view: View, layoutResId: Int) {
        val button = when (layoutResId) {
            R.layout.inapptut_screen1 -> view.findViewById<Button>(R.id.getStartedButton)
            R.layout.inapptut_screen2, 
            R.layout.inapptut_screen3, 
            R.layout.inapptut_screen4 -> view.findViewById<Button>(R.id.nextButton)
            R.layout.inapptut_screen5 -> view.findViewById<Button>(R.id.finishButton)
            else -> null
        }

        button?.setOnClickListener {
            when (layoutResId) {
                R.layout.inapptut_screen5 -> (activity as? InapptutActivity)?.finishTutorial()
                else -> (activity as? InapptutActivity)?.moveToNextPage()
            }
        }
    }
}