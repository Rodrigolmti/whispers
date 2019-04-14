package com.vortex.secret

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.vortex.secret.di.dataModule
import com.vortex.secret.di.viewModelModule
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(dataModule, viewModelModule))
        Fabric.with(this, Crashlytics())
    }
}