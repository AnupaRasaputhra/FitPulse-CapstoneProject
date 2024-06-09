package com.example.fitpulse

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitpulse.databinding.ActivityMainBinding
import com.example.fitpulse.ui.setgoals.SetGoalsDialogFragment
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel
import com.example.fitpulse.ui.slideshow.SlideshowFragment

class MainActivity : AppCompatActivity(), SlideshowFragment.OnSubmitClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var vM: SetGoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vM = ViewModelProvider(this)[SetGoalsViewModel::class.java]

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.set_goals, R.id.nav_bmi_calculator, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Pass MainActivity as the listener to the SlideshowFragment
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_slideshow)
        if (fragment is SlideshowFragment) {
            fragment.callback = this
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.set_goals -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.post {
                        val dialogFragment = SetGoalsDialogFragment()
                        dialogFragment.show(supportFragmentManager, "SetGoalsDialog")
                    }
                    true
                }
                else -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(menuItem.itemId)
                    true
                }
            }
        }

    }

    override fun onSubmitClicked(calculatedCalories: Double) {
        Log.d("MainActivity", "Calculated Calories: $calculatedCalories")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

