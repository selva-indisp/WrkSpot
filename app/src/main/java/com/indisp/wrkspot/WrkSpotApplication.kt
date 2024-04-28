package com.indisp.wrkspot

import android.app.Application
import com.indisp.country.di.countryDiModule
import com.indisp.network.networkDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WrkSpotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WrkSpotApplication)
            modules(appDiModule, networkDiModule, countryDiModule)
        }
    }
}