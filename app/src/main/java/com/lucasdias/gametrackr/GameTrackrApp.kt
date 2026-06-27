package com.lucasdias.gametrackr

import android.app.Application
import com.lucasdias.gametrackr.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GameTrackrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GameTrackrApp)
            modules(appModule)
        }
    }
}
