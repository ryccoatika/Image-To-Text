package com.ryccoatika.imagetotext.core.data.local.room

import androidx.room.*
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TextScannedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(textScanned: TextScannedEntity)

    @Delete
    suspend fun remove(textScanned: TextScannedEntity)

    @Query("SELECT * FROM TextScannedEntity WHERE text LIKE :query OR :query IS NULL ORDER BY dateTime DESC")
    fun getAll(query: String?): Flow<List<TextScannedEntity>>
}