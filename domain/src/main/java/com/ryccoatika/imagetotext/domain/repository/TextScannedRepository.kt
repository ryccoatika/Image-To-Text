package com.ryccoatika.imagetotext.domain.repository

import kotlinx.coroutines.flow.Flow

interface TextScannedRepository {
    suspend fun getIsUserFirstTime(): Boolean

    suspend fun setUserFirstTime(isFirstTime: Boolean)

    suspend fun saveTextScanned(textScanned: com.ryccoatika.imagetotext.domain.model.TextScanned)

    suspend fun removeTextScanned(textScanned: com.ryccoatika.imagetotext.domain.model.TextScanned)

    fun getAllTextScanned(query: String?): Flow<List<com.ryccoatika.imagetotext.domain.model.TextScanned>>
}