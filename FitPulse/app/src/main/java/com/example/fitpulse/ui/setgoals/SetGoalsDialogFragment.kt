package com.example.fitpulse.ui.setgoals

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SetGoalsDialogFragment : DialogFragment() {

    private var selectedItems: Int = 0
    private val goalsOptions = arrayOf("5000 Activate your body", "10000 Keep fit", "15000 Full-Body works",)

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alert = MaterialAlertDialogBuilder(requireContext()).setTitle("Set Daily Steps Goals")
            .setSingleChoiceItems(goalsOptions, selectedItems) { dialog, which ->
                selectedItems = which
            }
            .setPositiveButton("Ok") { dialog, which ->

            }
            .setNegativeButton("Cancel") { dialog, which -> }
            .create()
        return alert
    }
}