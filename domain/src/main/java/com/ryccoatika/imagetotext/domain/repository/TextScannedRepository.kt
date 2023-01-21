package com.ryccoatika.imagetotext.domain.repository

import android.net.Uri
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import kotlinx.coroutines.flow.Flow

interface TextScannedRepository {
    suspend fun getIsUserFirstTime(): Boolean

    suspend fun setUserFirstTime(isFirstTime: Boolean)

    suspend fun saveTextScanned(
        imageUri: Uri,
        textRecognized: TextRecognized,
        text: String
    ): TextScanned

    suspend fun removeTextScanned(textScanned: TextScanned)

    fun getAllTextScanned(query: String?): Flow<List<TextScanned>>

    suspend fun getTextScanned(id: Long): TextScanned
}