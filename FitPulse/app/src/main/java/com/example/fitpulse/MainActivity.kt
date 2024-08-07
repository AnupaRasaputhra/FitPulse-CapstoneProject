package com.example.fitpulse

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.fitpulse.databinding.ActivityMainBinding
import com.example.fitpulse.ui.inapptutorial.InapptutActivity
import com.example.fitpulse.ui.setgoals.SetGoalsDialogFragment
import com.example.fitpulse.ui.setgoals.SetGoalsViewModel
import com.example.fitpulse.ui.settings.SettingsActivity
import com.example.fitpulse.ui.slideshow.SlideshowFragment

class MainActivity : AppCompatActivity(), SlideshowFragment.OnSubmitClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var vM: SetGoalsViewModel
    private val CHANNEL_ID = "step_goal_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply theme based on saved preference
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        vM = ViewModelProvider(this)[SetGoalsViewModel::class.java]

        setSupportActionBar(binding.appBarMain.toolbar)
        createNotificationChannel()

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView


//        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.set_goals, R.id.nav_bmi_calculator, R.id.nav_slideshow, R.id.nav_progress_summary
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
                R.id.nav_tutorial -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, InapptutActivity::class.java))
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


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun pushNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Target Reached!")
            .setContentText("Congratulations! You've reached your step goal for today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
            return
        }
        notificationManager.notify(1, builder.build())
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                pushNotification()
            } else {
                Log.e("MainActivity", "Notification permission denied")
            }
        }
    }

    fun setActionBarTitle(title: String) {
        binding.appBarMain.toolbar.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tutorial -> {
                startActivity(Intent(this, InapptutActivity::class.java))
                true
            }
            R.id.action_settings -> {
                // Start the settings activity
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}