package com.tamagotchi.code.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tamagotchi.code.CodeTamagotchiApp
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.AppDatabase
import org.koin.java.KoinJavaComponent.get

class PetCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @android.annotation.SuppressLint("MissingPermission", "NotificationPermission")
    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend() ?: return Result.success()

        if (petState.isDead) return Result.success()

        val isHungry = petState.hunger < 20f
        val isSick = petState.health < 20f
        val isTired = petState.energy < 15f

        if (!isHungry && !isSick && !isTired) return Result.success()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val isCritical = petState.health < 5f || petState.hunger < 5f || petState.energy < 5f

        val channelId = when {
            petState.health < 5f || petState.hunger < 5f || petState.energy < 5f -> "pet_critical"
            petState.hunger < 20f -> "pet_hunger"
            petState.health < 20f -> "pet_health"
            petState.energy < 15f -> "pet_energy"
            else -> "pet_care_reminder"
        }

        val title = when {
            petState.health <= 0 -> applicationContext.getString(R.string.notif_title_dead, petState.name)
            petState.health < 5 -> applicationContext.getString(R.string.notif_title_dying, petState.name)
            petState.hunger < 5 -> applicationContext.getString(R.string.notif_title_starving, petState.name)
            petState.energy < 5 -> applicationContext.getString(R.string.notif_title_exhausted, petState.name)
            else -> applicationContext.getString(R.string.notif_title_needs_you, petState.name)
        }

        val reason = when {
            petState.health <= 0f -> applicationContext.getString(R.string.notif_reason_dead)
            petState.health < 5f -> applicationContext.getString(R.string.notif_reason_health_pain, petState.health.toInt())
            petState.health < 20f -> applicationContext.getString(R.string.notif_reason_health_low, petState.health.toInt())
            petState.hunger < 5f -> applicationContext.getString(R.string.notif_reason_hunger_extreme, petState.hunger.toInt())
            petState.hunger < 20f -> applicationContext.getString(R.string.notif_reason_hunger_loop, petState.hunger.toInt())
            petState.energy < 5f -> applicationContext.getString(R.string.notif_reason_energy_red, petState.energy.toInt())
            petState.energy < 15f -> applicationContext.getString(R.string.notif_reason_energy_logs, petState.energy.toInt())
            else -> applicationContext.getString(R.string.notif_reason_general)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.ic_stat_codey)
            .setContentTitle(title)
            .setContentText(reason)
            .setPriority(if (isCritical) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1001, notification)
        return Result.success()
    }
}