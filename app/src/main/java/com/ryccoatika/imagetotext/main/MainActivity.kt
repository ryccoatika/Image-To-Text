package com.ryccoatika.imagetotext.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.home.Home
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }

    @Composable
    private fun MainContent() {
        AppTheme(
            darkTheme = false
        ) {
            Home()
        }
    }
}