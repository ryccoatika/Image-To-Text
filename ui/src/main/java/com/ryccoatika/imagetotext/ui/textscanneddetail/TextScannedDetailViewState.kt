package com.ryccoatika.imagetotext.ui.textscanneddetail

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.UiMessage

data class TextScannedDetailViewState(
    val mode: Mode = Mode.VIEW,
    val textScanned: TextScanned? = null,
    val processing: Boolean = false,
    val message: UiMessage? = null
) {

    enum class Mode {
        VIEW, EDIT
    }

    companion object {
        val Empty = TextScannedDetailViewState()
    }
}