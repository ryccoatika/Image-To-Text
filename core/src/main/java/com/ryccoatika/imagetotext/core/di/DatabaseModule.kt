package com.ryccoatika.imagetotext.core.di

import android.content.Context
import com.ryccoatika.imagetotext.core.data.local.room.AppDatabase
import com.ryccoatika.imagetotext.core.data.local.room.TextScannedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)
}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {
    @Provides
    fun provideTextScannedDao(db: AppDatabase): TextScannedDao = db.textScannedDao()
}