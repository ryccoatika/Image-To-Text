package com.ryccoatika.imagetotext.ui.home

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.UiMessage

data class HomeViewState(
    val query: String? = null,
    val loading: Boolean = false,
    val processing: Boolean = false,
    val textScannedCollection: List<TextScanned> = emptyList(),
    val uiMessage: UiMessage? = null,
    val event: Event? = null
) {
    companion object {
        val Empty = HomeViewState()
    }

    sealed class Event {
        data class OpenTextScannedDetail(val textScanned: TextScanned) : Event()
    }
}