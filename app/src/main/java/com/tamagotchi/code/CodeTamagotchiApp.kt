package com.tamagotchi.code

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tamagotchi.code.di.appModule
import com.tamagotchi.code.util.CommitWorker
import com.tamagotchi.code.util.PetCheckWorker
import com.tamagotchi.code.widget.WidgetUpdateWorker
import com.tamagotchi.code.widget.CodePetWidgetProvider
import com.tamagotchi.code.data.repository.PetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class CodeTamagotchiApp : Application() {
    private val widgetScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        val koin = startKoin {
            androidContext(this@CodeTamagotchiApp)
            modules(appModule)
        }
        createNotificationChannels()
        schedulePetCheck()
        scheduleWidgetUpdate()
        scheduleDailyCommit()
        // Observe Room rather than a screen's lifecycle: updates also cover background writes.
        widgetScope.launch {
            val manager = AppWidgetManager.getInstance(this@CodeTamagotchiApp)
            val provider = ComponentName(this@CodeTamagotchiApp, CodePetWidgetProvider::class.java)
            koin.koin.get<PetRepository>().petState.distinctUntilChanged().collect { state ->
                manager.getAppWidgetIds(provider).forEach { id ->
                    CodePetWidgetProvider.updateAppWidget(this@CodeTamagotchiApp, manager, id, state)
                }
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val groupId = "pet_care"
            NotificationChannelGroup(groupId, getString(R.string.channel_group_care)).let { manager.createNotificationChannelGroup(it) }

            val channels = listOf(
                NotificationChannel("pet_hunger", getString(R.string.channel_hunger_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = getString(R.string.channel_hunger_desc)
                    this.group = groupId
                },
                NotificationChannel("pet_health", getString(R.string.channel_health_name), NotificationManager.IMPORTANCE_HIGH).apply {
                    description = getString(R.string.channel_health_desc)
                    this.group = groupId
                },
                NotificationChannel("pet_energy", getString(R.string.channel_energy_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = getString(R.string.channel_energy_desc)
                    this.group = groupId
                },
                NotificationChannel("pet_critical", getString(R.string.channel_critical_name), NotificationManager.IMPORTANCE_HIGH).apply {
                    description = getString(R.string.channel_critical_desc)
                    this.group = groupId
                },
                NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.channel_reminders_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = getString(R.string.channel_reminders_desc)
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

    private fun scheduleDailyCommit() {
        val now = java.util.Calendar.getInstance()
        val midnight = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        val delay = midnight.timeInMillis - now.timeInMillis

        val request = OneTimeWorkRequestBuilder<CommitWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("daily_commit")
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "daily_commit",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pet_care_reminder"
    }
}
