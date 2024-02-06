package com.ryccoatika.imagetotext.ui.imagepreview

import android.net.Uri
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.UiMessage

data class ImagePreviewViewState(
    val processing: Boolean = false,
    val imageUri: Uri = Uri.EMPTY,
    val languageModel: RecognationLanguageModel? = null,
    val isValid: Boolean = false,
    val message: UiMessage? = null,
    val event: Event? = null,
) {
    companion object {
        val Empty = ImagePreviewViewState()
    }

    sealed class Event {
        data class OpenTextScannedDetail(val textScanned: TextScanned) : Event()
    }
}
