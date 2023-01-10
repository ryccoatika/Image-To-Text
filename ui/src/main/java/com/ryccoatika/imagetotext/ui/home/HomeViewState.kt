package com.ryccoatika.imagetotext.ui.home

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.UiMessage

data class HomeViewState(
    val query: String? = null,
    val loading: Boolean = false,
    val processing: Boolean = false,
    val uiMessage: UiMessage? = null,
    val textScannedCollection: List<TextScanned> = emptyList()
) {
    companion object {
        val Empty = HomeViewState()
    }
}