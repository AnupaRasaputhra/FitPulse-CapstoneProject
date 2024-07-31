package com.example.fitpulse.ui.inapptutorial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fitpulse.R

class InapptutPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InapptutFragment.newInstance(R.layout.inapptut_screen1)
            1 -> InapptutFragment.newInstance(R.layout.inapptut_screen2)
             2 -> InapptutFragment.newInstance(R.layout.inapptut_screen3)
            3 -> InapptutFragment.newInstance(R.layout.inapptut_screen4)
            4 -> InapptutFragment.newInstance(R.layout.inapptut_screen5)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}