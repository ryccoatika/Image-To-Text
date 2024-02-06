package com.ryccoatika.imagetotext.core.data.local

import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.core.data.local.room.TextScannedDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LocalDataSource @Inject constructor(
    private val textScannedDao: TextScannedDao,
) {
    suspend fun saveTextScanned(textScanned: TextScannedEntity): TextScannedEntity {
        val textScannedId = textScannedDao.save(textScanned)
        return getTextScanned(textScannedId)
    }

    suspend fun removeTextScanned(textScanned: TextScannedEntity) =
        textScannedDao.remove(textScanned)

    fun getAllTextScanned(query: String?): Flow<List<TextScannedEntity>> {
        val q = if (query.isNullOrEmpty()) {
            null
        } else {
            "%$query%"
        }
        return textScannedDao.getAll(q)
    }

    suspend fun getTextScanned(id: Long): TextScannedEntity =
        textScannedDao.getById(id)
}
