package com.ryccoatika.imagetotext.ui.splash

data class SplashViewState(
    val isFirstTime: Boolean? = null
) {
    companion object {
        val Empty = SplashViewState()
    }
}