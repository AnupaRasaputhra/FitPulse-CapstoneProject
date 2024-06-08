package com.example.fitpulse.ui.setgoals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitpulse.R
import org.w3c.dom.Text

class SetGoalsFragment : Fragment() {

//    private val viewModel: SetGoalsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setgoals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SetGoalsDialogFragment().show(requireActivity().supportFragmentManager,"")

        val textView: TextView = view.findViewById(R.id.datas)

////         Observe the selected goal
//        viewModel.selectedGoals.observe(viewLifecycleOwner) { selectedGoal ->
//
//            textView.text = selectedGoal
//            requireActivity().findViewById<TextView>(R.id.targetSteps).text= "afkdf"
//
//            Toast.makeText(context, "Selected goal: $selectedGoal", Toast.LENGTH_SHORT).show()
//        }
    }

}