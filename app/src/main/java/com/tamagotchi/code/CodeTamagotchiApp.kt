package com.tamagotchi.code

import android.app.Application
import com.tamagotchi.code.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CodeTamagotchiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CodeTamagotchiApp)
            modules(appModule)
        }
    }
}
