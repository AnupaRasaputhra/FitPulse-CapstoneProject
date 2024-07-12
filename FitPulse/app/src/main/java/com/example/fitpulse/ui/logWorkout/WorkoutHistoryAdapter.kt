package com.example.fitpulse.ui.logWorkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpulse.R

class WorkoutHistoryAdapter(private val context: Context, var history: MutableList<Workout>) : RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.log_design, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTv)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTv)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTv)

        fun bind(workout: Workout) {
            titleTextView.text = workout.title
            descriptionTextView.text = workout.description ?: ""
            timeTextView.text = workout.duration.toString()
        }
    }
}
