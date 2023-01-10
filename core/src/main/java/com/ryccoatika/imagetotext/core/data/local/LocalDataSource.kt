package com.ryccoatika.imagetotext.core.data.local

import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.core.data.local.room.TextScannedDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val textScannedDao: TextScannedDao
) {
    suspend fun saveTextScanned(textScanned: TextScannedEntity) =
        textScannedDao.save(textScanned)

    suspend fun removeTextScanned(textScanned: TextScannedEntity) =
        textScannedDao.remove(textScanned)

    fun getAllTextScanned(query: String?): Flow<List<TextScannedEntity>> {
        val q = if (query == null || query.isEmpty()) {
            null
        } else {
            "%$query%"
        }
        return textScannedDao.getAll(q)
    }
}