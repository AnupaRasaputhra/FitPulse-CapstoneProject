package com.example.fitpulse.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitpulse.MainActivity
import com.example.fitpulse.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fitLogo = binding.logo
        val circleBackground = binding.circleBackground

        fitLogo.visibility = ImageView.VISIBLE
        circleBackground.visibility = ImageView.VISIBLE

        val scaleAnimation = ScaleAnimation(
            1.0f, 1.05f,
            1.0f, 1.05f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1500
            fillAfter = true
        }

        val fadeOutAnimation = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 1500
            fillAfter = true
        }

        fitLogo.startAnimation(scaleAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            circleBackground.visibility = ImageView.VISIBLE
            circleBackground.startAnimation(fadeOutAnimation)

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1500)
        }, 1500)
    }
}
