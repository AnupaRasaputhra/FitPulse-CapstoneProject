package com.example.fitpulse.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitpulse.R
import com.example.fitpulse.databinding.FragmentHomeBinding
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel

class HomeFragment : Fragment() {
    private val viewModel: SetGoalsViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedGoals.observe(viewLifecycleOwner) { goals ->

            requireActivity().findViewById<TextView>(R.id.targetSteps).text = goals


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
