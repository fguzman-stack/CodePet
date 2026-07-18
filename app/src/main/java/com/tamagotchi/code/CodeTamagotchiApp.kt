package com.tamagotchi.code

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tamagotchi.code.di.appModule
import com.tamagotchi.code.util.PetCheckWorker
import com.tamagotchi.code.widget.WidgetUpdateWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class CodeTamagotchiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CodeTamagotchiApp)
            modules(appModule)
        }
        createNotificationChannels()
        schedulePetCheck()
        scheduleWidgetUpdate()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val groupId = "pet_care"
            NotificationChannelGroup(groupId, "Cuidado de Codey").let { manager.createNotificationChannelGroup(it) }

            val channels = listOf(
                NotificationChannel("pet_hunger", "Hambre de Codey", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Codey tiene hambre"
                    this.group = groupId
                },
                NotificationChannel("pet_health", "Salud de Codey", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Codey está enfermo"
                    this.group = groupId
                },
                NotificationChannel("pet_energy", "Energía de Codey", NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Codey está cansado"
                    this.group = groupId
                },
                NotificationChannel("pet_critical", "Codey en peligro", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Codey está en estado crítico"
                    this.group = groupId
                }
            )
            channels.forEach { manager.createNotificationChannel(it) }
        }
    }

    private fun schedulePetCheck() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<PetCheckWorker>(
            4, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setInitialDelay(2, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "pet_check",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun scheduleWidgetUpdate() {
        val request = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            1, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pet_care_reminder"
    }
}