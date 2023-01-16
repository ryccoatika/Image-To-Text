package com.ryccoatika.imagetotext.ui.convertresult

import android.net.Uri
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel

data class ImageConvertResultViewState(
    val imageUri: Uri = Uri.EMPTY,
    val language: RecognationLanguageModel = RecognationLanguageModel.LATIN,
    val text: Text? = null
) {
    companion object {
        val Empty = ImageConvertResultViewState()
    }
}