package com.ryccoatika.imagetotext

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1000)
    }
}