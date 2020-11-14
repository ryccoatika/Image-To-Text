package com.ryccoatika.imagetotext.core.data.local

import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import com.ryccoatika.imagetotext.core.data.local.room.TextScannedDao
import io.reactivex.Completable
import io.reactivex.Flowable

class LocalDataSource(
    private val textScannedDao: TextScannedDao
) {
    fun insertTextScanned(textScanned: TextScannedEntity): Completable =
        textScannedDao.insertTextScanned(textScanned)

    fun deleteTextScanned(textScanned: TextScannedEntity): Completable =
        textScannedDao.deleteTextScanned(textScanned)

    fun getAllTextScanned(): Flowable<List<TextScannedEntity>> =
        textScannedDao.getAllTextScanned()

    fun searchTextScanned(query: String): Flowable<List<TextScannedEntity>> =
        textScannedDao.searchTextScanned("%$query%")
}