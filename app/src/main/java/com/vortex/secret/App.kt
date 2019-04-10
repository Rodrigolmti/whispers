package com.vortex.secret

import android.app.Application
import com.vortex.secret.di.dataModule
import com.vortex.secret.di.viewModelModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(dataModule, viewModelModule))
    }
}