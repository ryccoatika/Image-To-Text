package com.ryccoatika.imagetotext.main

data class MainViewState(
    val isFirstTime: Boolean? = null
) {
    companion object {
        val Empty = MainViewState()
    }
}