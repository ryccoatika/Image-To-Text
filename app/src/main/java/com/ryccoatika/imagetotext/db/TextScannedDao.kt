package com.ryccoatika.imagetotext.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface TextScannedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(textScanned: TextScannedEntity) : Single<Long>

    @Delete
    fun delete(textScanned: TextScannedEntity) : Completable

    @Query("SELECT * FROM ${TextScannedEntity.TABLE_NAME}")
    fun getTexts(): Single<List<TextScannedEntity>>

    @Query("SELECT * FROM ${TextScannedEntity.TABLE_NAME} WHERE ${TextScannedEntity.TEXT} LIKE :search")
    fun searchTexts(search: String) : Single<List<TextScannedEntity>>
}