package com.ryccoatika.imagetotext.domain.model

import android.net.Uri
import android.util.Size

data class TextScanned(
    val id: Long,
    val imageUri: Uri,
    val imageSize: Size,
    var text: String,
    val textRecognized: TextRecognized,
)
