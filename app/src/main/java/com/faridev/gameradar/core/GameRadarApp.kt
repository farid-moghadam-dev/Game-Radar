package com.faridev.gameradar.core

import android.app.Application
import com.faridev.gameradar.BuildConfig
import com.faridev.gameradar.di.networkModule
import com.faridev.gameradar.di.repositoryModule
import com.faridev.gameradar.di.useCaseModule
import com.faridev.gameradar.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class GameRadarApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GameRadarApp)
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
