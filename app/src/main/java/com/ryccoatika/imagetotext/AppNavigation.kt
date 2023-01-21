package com.ryccoatika.imagetotext

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.ryccoatika.imagetotext.ui.convertresult.ImageConvertResult
import com.ryccoatika.imagetotext.ui.home.Home
import com.ryccoatika.imagetotext.ui.intro.Intro
import com.ryccoatika.imagetotext.ui.languagemodelselect.LanguageModelSelect
import com.ryccoatika.imagetotext.ui.splash.Splash

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
    object HomeScreen : LeafScreen("home/{uri}") {
        fun createRoute(
            root: Screen,
            uri: Uri?
        ): String = "${root.route}/home/${uri?.let { Uri.encode(it.toString()) }}"
    }

    object ImageConvertResultScreen : LeafScreen("imageconvertresult/{id}") {
        fun createRoute(
            root: Screen,
            id: Long
        ): String = "${root.route}/imageconvertresult/$id"
    }

    object LanguageModelSelectorScreen : LeafScreen("languagemodelselector")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
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
        addSplashScreen(Screen.Splash, navController)
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
        addImageConvertResultScreen(Screen.Home, navController)
        addLanguageModelSelector(Screen.Home, navController)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.addSplashScreen(
    root: Screen,
    navController: NavController
) {
    composable(
        route = LeafScreen.SplashScreen.createRoute(root),
    ) {
        Splash(
            openHomeScreen = {
                navController.navigate(Screen.Home.route) {
                    launchSingleTop = true

                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            },
            openIntroScreen = {
                navController.navigate(LeafScreen.IntroScreen.createRoute(root)) {
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
            savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
            openImageResultScreen = { uri ->
                navController.navigate(LeafScreen.ImageConvertResultScreen.createRoute(root, uri))
            },
            openLanguageSelectorScreen = {
                navController.navigate(LeafScreen.LanguageModelSelectorScreen.createRoute(root))
            }
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

private fun NavGraphBuilder.addLanguageModelSelector(
    root: Screen,
    navController: NavController
) {
    dialog(
        route = LeafScreen.LanguageModelSelectorScreen.createRoute(root),
    ) {
        LanguageModelSelect(
            languageModelSelected = { langModel ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "languagemodel",
                    langModel.name
                )
                navController.popBackStack()
                Log.i("190401", "addLanguageModelSelector: ")
            }
        )
    }
}