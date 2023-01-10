package com.ryccoatika.imagetotext.ui.textscanneddetail

import com.ryccoatika.imagetotext.domain.utils.UiMessage

data class TextScannedDetailViewState(
    val processing: Boolean = false,
    val message: UiMessage? = null
) {
    companion object {
        val Empty = TextScannedDetailViewState()
    }
}