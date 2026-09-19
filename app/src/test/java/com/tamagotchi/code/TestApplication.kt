package com.tamagotchi.code

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

class TestApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    // Robolectric can reuse WorkManager's singleton across tests in the same sandbox.
    try {
      WorkManager.getInstance(this)
    } catch (_: IllegalStateException) {
      WorkManager.initialize(
        this,
        Configuration.Builder().setMinimumLoggingLevel(Log.ERROR).build()
      )
    }
  }
}
