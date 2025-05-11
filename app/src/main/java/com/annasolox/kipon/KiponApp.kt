package com.annasolox.kipon

import android.app.Application
import com.annasolox.kipon.core.appModule
import com.annasolox.kipon.data.dataModule
import com.annasolox.kipon.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class KiponApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@KiponApp)
            modules(appModule, dataModule, viewModelModule)
        }
    }
}