package com.abcloudz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

            showSplash(BuildConfig.SPLASH_TIME)
    }

    private fun showSplash(time: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            showMainScreen()
        }, time)
    }

    private fun showMainScreen(){
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
        finish()
    }
}