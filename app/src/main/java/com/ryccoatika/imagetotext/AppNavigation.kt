package com.ryccoatika.imagetotext

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.ryccoatika.imagetotext.ui.convertresult.ImageConvertResult
import com.ryccoatika.imagetotext.ui.home.Home
import com.ryccoatika.imagetotext.ui.imagepreview.ImagePreview

internal sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
}

internal sealed class LeafScreen(
    private val route: String,
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    data object SplashScreen : LeafScreen("splash")
    data object HomeScreen : LeafScreen("home")

    data object ImageConvertResultScreen : LeafScreen("imageconvertresult/{id}") {
        fun createRoute(
            root: Screen,
            id: Long,
        ): String = "${root.route}/imageconvertresult/$id"
    }

    data object ImagePreview : LeafScreen("languagemodelselector/{uri}") {
        fun createRoute(
            root: Screen,
            uri: Uri?,
        ): String = "${root.route}/languagemodelselector/${uri?.let { Uri.encode(it.toString()) }}"
    }
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        addSplashTopLevel()
        addHomeTopLevel(navController)
    }
}

private fun NavGraphBuilder.addSplashTopLevel() {
    navigation(
        route = Screen.Splash.route,
        startDestination = LeafScreen.SplashScreen.createRoute(Screen.Splash),
    ) {
        addSplashScreen(Screen.Splash)
    }
}

private fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.HomeScreen.createRoute(Screen.Home),
    ) {
        addHomeScreen(Screen.Home, navController)
        addImagePreview(Screen.Home, navController)
        addImageConvertResultScreen(Screen.Home, navController)
    }
}

private fun NavGraphBuilder.addSplashScreen(
    root: Screen,
) {
    composable(
        route = LeafScreen.SplashScreen.createRoute(root),
    ) {}
}

private fun NavGraphBuilder.addHomeScreen(
    root: Screen,
    navController: NavController,
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

private fun NavGraphBuilder.addImageConvertResultScreen(
    root: Screen,
    navController: NavController,
) {
    composable(
        route = LeafScreen.ImageConvertResultScreen.createRoute(root),
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            },
        ),
    ) {
        ImageConvertResult(
            navigateBack = navController::navigateUp,
        )
    }
}

private fun NavGraphBuilder.addImagePreview(
    root: Screen,
    navController: NavController,
) {
    composable(
        route = LeafScreen.ImagePreview.createRoute(root),
        arguments = listOf(
            navArgument("uri") {
                type = NavType.StringType
            },
        ),
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
            navigateUp = navController::navigateUp,
        )
    }
}
