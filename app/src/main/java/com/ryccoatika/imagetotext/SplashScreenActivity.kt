package com.ryccoatika.imagetotext

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.ryccoatika.imagetotext.core.utils.UserPreferences
import com.ryccoatika.imagetotext.home.HomeActivity
import com.ryccoatika.imagetotext.intro.IntroActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : AppCompatActivity() {

    private val userPref: UserPreferences by lazy { UserPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(mainLooper).postDelayed({
            userPref.isFirstTime.asLiveData().observe(this) { isFirstTime ->
                if (isFirstTime) {
                    startActivity(Intent(this, IntroActivity::class.java))
                    runBlocking {
                        launch(Dispatchers.IO) {
                            userPref.saveFirstTime(true)
                        }
                    }
                } else
                    startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }, 1000)
    }
}