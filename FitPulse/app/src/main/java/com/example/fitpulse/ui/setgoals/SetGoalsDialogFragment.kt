package com.example.fitpulse.ui.setgoals

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitpulse.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SetGoalsDialogFragment : DialogFragment() {

    private var selectedItems: Int = 0
    private val goalsOptions = arrayOf("5000", "10000", "15000")
    private val viewModel: SetGoalsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alert = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Set Daily Steps Goals")
            .setSingleChoiceItems(goalsOptions, selectedItems) { dialog, which ->
                selectedItems = which
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.setSelectedGoals(goalsOptions[selectedItems])
            }
            .setNegativeButton("Cancel") { dialog, which -> }
            .create()
        return alert
    }
}
