package com.ryccoatika.imagetotext.core.di

import com.ryccoatika.imagetotext.core.data.TextRecognitionRepositoryImpl
import com.ryccoatika.imagetotext.core.data.TextScannedRepositoryImpl
import com.ryccoatika.imagetotext.domain.repository.TextRecognitionRepository
import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryBindsModule {

    @Binds
    abstract fun provideTextScannedRepository(repository: TextScannedRepositoryImpl): TextScannedRepository

    @Binds
    abstract fun provideTextRecognitionRepository(repository: TextRecognitionRepositoryImpl): TextRecognitionRepository
}
