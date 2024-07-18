package com.example.fitpulse.ui.footstepsHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.fitpulse.R


class FootStepsHistoryFragment : Fragment() {

    private val footStepsHistoryViewModel: FootStepsHistoryViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foot_steps_history, container, false)

        footStepsHistoryViewModel.stepsData.observe(viewLifecycleOwner) { stepsData ->
            view.findViewById<TextView>(R.id.sundayData).text = stepsData["SUNDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.mondayData).text = stepsData["MONDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.tuesdayData).text = stepsData["TUESDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.wednesdayData).text = stepsData["WEDNESDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.thursdayData).text = stepsData["THURSDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.fridayData).text = stepsData["FRIDAY"]?.toString() ?: ""
            view.findViewById<TextView>(R.id.SaturdayData).text = stepsData["SATURDAY"]?.toString() ?: ""
        }

        return view
    }
}