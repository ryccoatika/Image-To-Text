package com.ryccoatika.imagetotext.core.di

import com.ryccoatika.imagetotext.core.data.TextScannedRepository
import com.ryccoatika.imagetotext.core.data.local.LocalDataSource
import com.ryccoatika.imagetotext.core.data.local.room.AppDatabase
import com.ryccoatika.imagetotext.core.domain.repository.ITextScannedRepository
import com.ryccoatika.imagetotext.core.domain.usecase.TextScannedInteractor
import com.ryccoatika.imagetotext.core.domain.usecase.TextScannedUseCase
import org.koin.dsl.module

val roomModule = module {
    single { AppDatabase.getInstance(get()).textScannedDao() }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single<ITextScannedRepository> { TextScannedRepository(get()) }
}

val useCaseModule = module {
    single<TextScannedUseCase> { TextScannedInteractor(get()) }
}