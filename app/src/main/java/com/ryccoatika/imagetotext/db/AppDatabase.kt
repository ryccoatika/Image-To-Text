package com.ryccoatika.imagetotext.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TextScannedEntity::class], version = AppDatabase.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun textScannedDao(): TextScannedDao
    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "ImageToTextDB"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyDatabase() {
            INSTANCE = null
        }
    }
}