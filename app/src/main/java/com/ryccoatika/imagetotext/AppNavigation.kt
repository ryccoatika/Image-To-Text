package com.ryccoatika.imagetotext

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ryccoatika.imagetotext.ui.convertresult.ImageConvertResult
import com.ryccoatika.imagetotext.ui.home.Home
import com.ryccoatika.imagetotext.ui.intro.Intro
import com.ryccoatika.imagetotext.ui.imagepreview.ImagePreview

internal sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
}

internal sealed class LeafScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object SplashScreen : LeafScreen("splash")
    object IntroScreen : LeafScreen("intro")
    object HomeScreen : LeafScreen("home")

    object ImageConvertResultScreen : LeafScreen("imageconvertresult/{id}") {
        fun createRoute(
            root: Screen,
            id: Long
        ): String = "${root.route}/imageconvertresult/$id"
    }

    object ImagePreview : LeafScreen("languagemodelselector/{uri}") {
        fun createRoute(
            root: Screen,
            uri: Uri?
        ): String = "${root.route}/languagemodelselector/${uri?.let { Uri.encode(it.toString()) }}"
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        addSplashTopLevel(navController)
        addHomeTopLevel(navController)
    }
}

private fun NavGraphBuilder.addSplashTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Splash.route,
        startDestination = LeafScreen.SplashScreen.createRoute(Screen.Splash)
    ) {
        addSplashScreen(Screen.Splash)
        addIntroScreen(Screen.Splash, navController)
    }
}

private fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.HomeScreen.createRoute(Screen.Home)
    ) {
        addHomeScreen(Screen.Home, navController)
        addImagePreview(Screen.Home, navController)
        addImageConvertResultScreen(Screen.Home, navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addSplashScreen(
    root: Screen
) {
    composable(
        route = LeafScreen.SplashScreen.createRoute(root),
    ) {}
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addIntroScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.IntroScreen.createRoute(root),
    ) {
        Intro(
            openHomeScreen = {
                navController.navigate(Screen.Home.route) {
                    launchSingleTop = true

                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addHomeScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.HomeScreen.createRoute(root),
    ) {
        Home(
            openImageResultScreen = {
                navController.navigate(LeafScreen.ImageConvertResultScreen.createRoute(root, it)) {
                    launchSingleTop = true
                }
            },
            openImagePreviewScreen = {
                navController.navigate(LeafScreen.ImagePreview.createRoute(root, it)) {
                    launchSingleTop = true
                }
            },
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addImageConvertResultScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.ImageConvertResultScreen.createRoute(root),
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )
    ) {
        ImageConvertResult(
            navigateBack = navController::navigateUp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addImagePreview(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.ImagePreview.createRoute(root),
        arguments = listOf(
            navArgument("uri") {
                type = NavType.StringType
            }
        )
    ) {
        ImagePreview(
            openImageResultScreen = {
                navController.navigate(LeafScreen.ImageConvertResultScreen.createRoute(root, it)) {
                    launchSingleTop = true


                    popUpTo(LeafScreen.ImagePreview.createRoute(root)) {
                        inclusive = true
                    }
                }
            },
            navigateUp = navController::navigateUp
        )
    }
}