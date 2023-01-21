package com.ryccoatika.imagetotext.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ryccoatika.imagetotext.AppNavigation
import com.ryccoatika.imagetotext.LeafScreen
import com.ryccoatika.imagetotext.Screen
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var intentListener: Consumer<Intent?>? = null

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberAnimatedNavController()
            MainContent(navHostController)
            DisposableEffect(Unit) {
                handleIntentAction(intent, navHostController)
                val listener = Consumer<Intent?> { intent ->
                    handleIntentAction(intent, navHostController)
                }
                intentListener = listener
                onDispose { intentListener = null }
            }
        }
    }

    @Composable
    private fun MainContent(
        navHostController: NavHostController
    ) {

        AppTheme(
            darkTheme = false
        ) {
            AppNavigation(navController = navHostController)
        }
    }

    private fun handleIntentAction(intent: Intent?, navController: NavController) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_SEND) {
            @Suppress("DEPRECATION")
            val uri: Uri? = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            } else {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            }
            uri?.let {
                navController.navigate(
                    LeafScreen.HomeScreen.createRoute(
                        Screen.Home,
                        it
                    )
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intentListener?.accept(intent)
    }
}