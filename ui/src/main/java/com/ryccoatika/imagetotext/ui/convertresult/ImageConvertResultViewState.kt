package com.ryccoatika.imagetotext.ui.convertresult

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel

data class ImageConvertResultViewState(
    val uri: Uri = Uri.EMPTY,
    val inputImage: InputImage? = null,
    val language: RecognationLanguageModel = RecognationLanguageModel.LATIN,
    val textBlocks: List<Text.TextBlock> = emptyList(),
    val elements: List<Text.Element> = emptyList(),
    val text: String = ""
) {
    companion object {
        val Empty = ImageConvertResultViewState()
    }
}