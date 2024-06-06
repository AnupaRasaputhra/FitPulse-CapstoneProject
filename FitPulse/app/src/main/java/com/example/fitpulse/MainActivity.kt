package com.example.fitpulse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitpulse.databinding.ActivityMainBinding
import com.example.fitpulse.ui.slideshow.SlideshowFragment

class MainActivity : AppCompatActivity(), SlideshowFragment.OnSubmitClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.set_goals, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Pass MainActivity as the listener to the SlideshowFragment
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_slideshow)
        if (fragment is SlideshowFragment) {
            fragment.callback = this
        }
    }

    // Implement the onSubmitClicked method to handle the data received from the SlideshowFragment
    override fun onSubmitClicked(calculatedBmi: Double) {
        // Handle the data here, for example, you can log it or perform any other actions
//        Log.d("MainActivity", "Age: $age, Height: $height, Weight: $weight, Gender: $gender, Goal: $goal")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

