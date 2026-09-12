package com.tamagotchi.code

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

class TestApplication : Application() {
  override fun onCreate() {
    WorkManager.initialize(
      this,
      Configuration.Builder().setMinimumLoggingLevel(Log.ERROR).build()
    )
    super.onCreate()
  }
}
