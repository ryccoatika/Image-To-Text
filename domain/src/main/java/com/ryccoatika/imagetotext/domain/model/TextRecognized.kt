package com.ryccoatika.imagetotext.domain.model

import android.graphics.Rect

data class TextRecognized(
    val text: String,
    val textBlocks: List<TextBlock>,
) {

    data class TextBlock(
        val text: String,
        val lines: List<Line>,
        val boundingBox: Rect?,
    )

    data class Line(
        val text: String,
        val elements: List<Element>,
    )

    data class Element(
        val text: String,
        val angle: Float,
        val boundingBox: Rect?,
    )
}
