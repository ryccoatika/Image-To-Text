package com.ryccoatika.imagetotext.core.repository

import com.ryccoatika.imagetotext.core.model.TextScanned
import kotlinx.coroutines.flow.Flow

interface TextScannedRepository {
    suspend fun getIsUserFirstTime(): Boolean

    suspend fun setUserFirstTime(isFirstTime: Boolean)

    suspend fun saveTextScanned(textScanned: TextScanned)

    suspend fun removeTextScanned(textScanned: TextScanned)

    fun getAllTextScanned(query: String?): Flow<List<TextScanned>>
}