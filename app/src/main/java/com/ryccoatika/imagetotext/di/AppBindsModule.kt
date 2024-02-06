package com.ryccoatika.imagetotext.di

import com.ryccoatika.imagetotext.domain.utils.ComposeFileProvider
import com.ryccoatika.imagetotext.utils.ComposeFileProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppBindsModule {
    @Binds
    abstract fun provideComposeFileProvider(composeFileProviderImpl: ComposeFileProviderImpl): ComposeFileProvider
}
