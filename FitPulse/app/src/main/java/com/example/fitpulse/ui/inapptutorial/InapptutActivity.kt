package com.example.fitpulse.ui.inapptutorial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.fitpulse.databinding.ActivityInapptutBinding
import android.view.View
import android.content.Intent
import com.example.fitpulse.MainActivity

class InapptutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInapptutBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInapptutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.adapter = InapptutPagerAdapter(this)
        viewPager.isUserInputEnabled = false
        binding.tabLayout.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        } else {
            super.onBackPressed()
        }
    }

    fun finishTutorial() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun moveToNextPage() {
        if (viewPager.currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
            viewPager.currentItem = viewPager.currentItem + 1
        }
    }
}