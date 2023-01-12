package com.ryccoatika.imagetotext.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ryccoatika.imagetotext.AppNavigation
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainContent() {
        val navHostController = rememberAnimatedNavController()

        AppTheme(
            darkTheme = false
        ) {
            AppNavigation(navController = navHostController)
        }
    }
}