package com.ryccoatika.imagetotext.domain.model

import android.net.Uri

data class TextScanned(
    val id: Long,
    val imageUri: Uri,
    var text: String,
    val textRecognized: TextRecognized
)