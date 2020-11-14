package com.ryccoatika.imagetotext.core.data.local.room

import androidx.room.*
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TextScannedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTextScanned(textScanned: TextScannedEntity): Completable

    @Delete
    fun deleteTextScanned(textScanned: TextScannedEntity): Completable

    @Query("SELECT * FROM TextScannedEntity ORDER BY dateTime DESC")
    fun getAllTextScanned(): Flowable<List<TextScannedEntity>>

    @Query("SELECT * FROM TEXTSCANNEDENTITY WHERE text LIKE :query")
    fun searchTextScanned(query: String) : Flowable<List<TextScannedEntity>>
}