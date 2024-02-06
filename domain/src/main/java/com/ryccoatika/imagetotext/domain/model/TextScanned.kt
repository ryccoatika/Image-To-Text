package com.ryccoatika.imagetotext.domain.model

import android.graphics.Bitmap

data class TextScanned(
    val id: Long,
    val image: Bitmap,
    var text: String,
    val textRecognized: TextRecognized,
)
