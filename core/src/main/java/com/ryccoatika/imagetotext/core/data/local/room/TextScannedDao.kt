package com.ryccoatika.imagetotext.core.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TextScannedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(textScanned: TextScannedEntity): Long

    @Delete
    suspend fun remove(textScanned: TextScannedEntity)

    @Query("SELECT * FROM TextScannedEntity WHERE text LIKE :query OR :query IS NULL ORDER BY id DESC")
    fun getAll(query: String?): Flow<List<TextScannedEntity>>

    @Query("SELECT * FROM TextScannedEntity WHERE id = :id")
    suspend fun getById(id: Long): TextScannedEntity
}
