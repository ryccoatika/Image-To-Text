package com.ryccoatika.imagetotext.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ryccoatika.imagetotext.AppNavigation
import com.ryccoatika.imagetotext.LeafScreen
import com.ryccoatika.imagetotext.Screen
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var splashScreen: SplashScreen? = null

    private var intentListener: Consumer<Intent?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen = installSplashScreen()
            splashScreen?.setKeepOnScreenCondition { true }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navHostController = rememberNavController()
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
        navHostController: NavHostController,
    ) {
        LaunchedEffect(Unit) {
            splashScreen?.setKeepOnScreenCondition { false }
        }

        AppTheme(
            darkTheme = false,
        ) {
            AppNavigation(
                navController = navHostController,
                modifier = Modifier.fillMaxSize(),
            )
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
                navController.navigate(Screen.Home.route) {
                    launchSingleTop = true

                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                navController.navigate(
                    LeafScreen.ImagePreview.createRoute(
                        Screen.Home,
                        it,
                    ),
                ) {
                    launchSingleTop = true
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intentListener?.accept(intent)
    }
}
