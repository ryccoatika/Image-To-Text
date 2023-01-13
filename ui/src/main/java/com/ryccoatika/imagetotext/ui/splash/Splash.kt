package com.ryccoatika.imagetotext.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle

@Composable
fun Splash(
    openHomeScreen: () -> Unit,
    openIntroScreen: () -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()

    Splash(
        viewModel = viewModel,
        openHomeScreen = openHomeScreen,
        openIntroScreen = openIntroScreen
    )
}

@Composable
private fun Splash(
    viewModel: SplashViewModel,
    openHomeScreen: () -> Unit,
    openIntroScreen: () -> Unit
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    Splash(
        state = viewState,
        openHomeScreen = openHomeScreen,
        openIntroScreen = openIntroScreen
    )
}

@Composable
private fun Splash(
    state: SplashViewState,
    openHomeScreen: () -> Unit,
    openIntroScreen: () -> Unit
) {
    SideEffect {
        if (state.isFirstTime == true) {
            openIntroScreen()
        } else if (state.isFirstTime == false) {
            openHomeScreen()
        }
    }
}

@Preview
@Composable
private fun SplashPreview() {
    AppTheme {
        Splash(
            openHomeScreen = {},
            openIntroScreen = {}
        )
    }
}