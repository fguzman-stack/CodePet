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
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import org.koin.java.KoinJavaComponent.get

class CommitWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend() ?: return Result.success()
        val prefs = get<UserPreferencesRepository>(UserPreferencesRepository::class.java)

        val activities = prefs.getDailyActivityLog()
        val commit = DailyCommitGenerator.generateCommit(activities, petState.name)
        prefs.setPendingCommit(commit)
        prefs.clearDailyActivityLog()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            "pet_care_reminder"
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("${petState.name} hizo commit")
            .setContentText(commit)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1002, notification)
        return Result.success()
    }
}