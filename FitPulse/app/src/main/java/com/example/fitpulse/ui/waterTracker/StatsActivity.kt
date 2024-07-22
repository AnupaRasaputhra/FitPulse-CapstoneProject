package com.example.fitpulse.ui.waterTracker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitpulse.R
import com.example.fitpulse.databinding.ActivityStatsBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val waterGoal = intent.getIntExtra("waterGoal", 0) ?: 0
        val waterIntake = intent.getIntExtra("waterIntake", 0) ?: 0

        binding.btnBack.setOnClickListener {
            finish()
        }

        val remaining = waterGoal - waterIntake
        binding.remainingIntake.text = "${if (remaining > 0) remaining else 0} ml"
        binding.targetIntake.text = "$waterGoal ml"

        // Calculate the percentage
        val percentage = (waterIntake * 100 / waterGoal) ?: 0

        binding.waterLevelView.centerTitle = "$percentage%"
        binding.waterLevelView.progressValue = percentage

        // Create chart data based on real values
        val entries = ArrayList<Entry>().apply {
            // Add start (0% progress)
            add(Entry(0f, 0f))
            // Add current water intake (show progress)
            add(Entry(1f, waterIntake.toFloat()))
            // Add water goal (target line)
            add(Entry(2f, waterGoal.toFloat()))
        }

        // Create dataset
        val dataSet = LineDataSet(entries, "Water Intake Progress")
        dataSet.setDrawCircles(true)
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)
        dataSet.lineWidth = 2.5f
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ContextCompat.getColor(this, R.color.colorPrimary)
        dataSet.setDrawValues(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        // Set data to the chart
        val lineData = LineData(dataSet)
        binding.chart.data = lineData

        // Configure the chart
        binding.chart.description.isEnabled = false
        binding.chart.animateY(1000, Easing.Linear)
        binding.chart.viewPortHandler.setMaximumScaleX(1.5f)
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.xAxis.position = XAxis.XAxisPosition.TOP
        binding.chart.xAxis.isGranularityEnabled = true
        binding.chart.legend.isEnabled = false
        binding.chart.fitScreen()
        binding.chart.isAutoScaleMinMaxEnabled = true
        binding.chart.scaleX = 1f
        binding.chart.setPinchZoom(true)
        binding.chart.isScaleXEnabled = true
        binding.chart.isScaleYEnabled = false
        binding.chart.axisLeft.textColor = Color.BLACK
        binding.chart.xAxis.textColor = Color.BLACK
        binding.chart.axisLeft.setDrawAxisLine(false)
        binding.chart.xAxis.setDrawAxisLine(false)
        binding.chart.setDrawMarkers(true)
        binding.chart.xAxis.labelCount = 3

        // Configure left axis
        val leftAxis = binding.chart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = maxOf(waterGoal.toFloat(), 100f) + 15f

        // Add limit line for water goal
        val targetLine = LimitLine(waterGoal.toFloat(), "Goal")
        targetLine.enableDashedLine(5f, 5f, 0f)
        targetLine.lineColor = Color.RED
        targetLine.textSize = 12f
        leftAxis.addLimitLine(targetLine)

        // Configure right axis
        val rightAxis = binding.chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawLabels(false)

        // Refresh chart
        binding.chart.invalidate()
    }
}