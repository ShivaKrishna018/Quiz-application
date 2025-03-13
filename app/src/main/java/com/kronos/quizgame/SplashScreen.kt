package com.kronos.quizgame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kronos.quizgame.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var splashBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = splashBinding.root
        setContentView(view)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anime)
        splashBinding.quizShow.startAnimation(animation)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

    }
}