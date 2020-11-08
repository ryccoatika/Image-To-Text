package com.ryccoatika.imagetotext.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ryccoatika.imagetotext.core.data.local.entity.TextScannedEntity

@Database(entities = [TextScannedEntity::class], version = AppDatabase.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun textScannedDao(): TextScannedDao
    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "ImageToTextDB"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase =
            INSTANCE ?: synchronized(AppDatabase::class) {
                val newInstance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
                INSTANCE = newInstance
                newInstance
            }

        fun destroyDatabase() {
            INSTANCE = null
        }
    }
}