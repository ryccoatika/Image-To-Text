package com.ryccoatika.imagetotext

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ryccoatika.imagetotext.home.HomeActivity
import com.ryccoatika.imagetotext.intro.IntroActivity

class SplashScreenActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val FIRST_TIME = "first_time"
        private const val PREFS_NAME = "user_pref"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            if (isFirstTime()) {
                startActivity(Intent(this, IntroActivity::class.java))
                setFirstTimeFalse()
            } else
                startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1000)
    }

    private fun isFirstTime(): Boolean = sharedPref.getBoolean(FIRST_TIME, true)

    private fun setFirstTimeFalse() {
        sharedPref.edit().putBoolean(FIRST_TIME, false).apply()
    }
}