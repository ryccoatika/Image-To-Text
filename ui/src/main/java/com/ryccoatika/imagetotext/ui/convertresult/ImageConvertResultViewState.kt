package com.ryccoatika.imagetotext.ui.convertresult

import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned

data class ImageConvertResultViewState(
    val textScanned: TextScanned? = null,
    val elements: List<TextRecognized.Element> = emptyList(),
    val event: Event? = null,
) {
    companion object {
        val Empty = ImageConvertResultViewState()
    }

    sealed class Event {
        data object RemoveSuccess : Event()
    }
}
