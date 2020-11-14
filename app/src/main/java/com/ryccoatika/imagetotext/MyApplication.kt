package com.ryccoatika.imagetotext

import android.app.Application
import com.ryccoatika.imagetotext.core.di.repositoryModule
import com.ryccoatika.imagetotext.core.di.roomModule
import com.ryccoatika.imagetotext.core.di.useCaseModule
import com.ryccoatika.imagetotext.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                roomModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}