package com.ryccoatika.imagetotext.domain.repository

import android.net.Uri
import android.util.Size
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import kotlinx.coroutines.flow.Flow

interface TextScannedRepository {
    suspend fun saveTextScanned(
        imageUri: Uri,
        imageSize: Size,
        textRecognized: TextRecognized,
        text: String,
    ): TextScanned

    suspend fun removeTextScanned(textScanned: TextScanned)

    fun getAllTextScanned(query: String?): Flow<List<TextScanned>>

    suspend fun getTextScanned(id: Long): TextScanned
}
