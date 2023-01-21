package com.ryccoatika.imagetotext.ui.convertresult

import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned

data class ImageConvertResultViewState(
    val textScanned: TextScanned? = null,
    val inputImage: InputImage? = null,
    val elements: List<TextRecognized.Element> = emptyList(),
) {
    companion object {
        val Empty = ImageConvertResultViewState()
    }
}