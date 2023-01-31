package com.ryccoatika.imagetotext.ui.home

import com.ryccoatika.imagetotext.domain.model.TextScanned

data class HomeViewState(
    val query: String? = null,
    val loading: Boolean = false,
    val processing: Boolean = false,
    val textScannedCollection: List<TextScanned> = emptyList()
) {
    companion object {
        val Empty = HomeViewState()
    }
}