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

    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend() ?: return Result.success()

        if (petState.isDead) return Result.success()

        val needsAttention = petState.hunger < 20f ||
                petState.health < 20f ||
                petState.energy < 15f

        if (!needsAttention) return Result.success()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val isCritical = petState.health < 5f || petState.hunger < 5f || petState.energy < 5f

        val reason = when {
            petState.health < 5f -> "Salud: ${petState.health.toInt()}% — " + when {
                petState.health <= 0f -> "Me he muerto... Bueno, fue divertido mientras dur\u00f3. O no."
                petState.health < 3f -> "Se me apaga el monitor... literalmente. Salud: ${petState.health.toInt()}%"
                else -> "Noto que el Blue Screen of Death se acerca... (Salud: ${petState.health.toInt()}%)"
            }
            petState.hunger < 5f -> "Me muero de hambre... y no es una met\u00e1fora. (Hambre: ${petState.hunger.toInt()}%)"
            petState.hunger < 20f -> "\u00a1Tengo hambre! (${petState.hunger.toInt()}%)"
            petState.health < 5f -> "Esto es peor que un NullPointerException... (Salud: ${petState.health.toInt()}%)"
            petState.health < 20f -> "No me siento bien... (Salud: ${petState.health.toInt()}%)"
            petState.energy < 5f -> "Modo ahorro de energ\u00eda activado. O sea, me muero. (Energ\u00eda: ${petState.energy.toInt()}%)"
            petState.energy < 15f -> "Estoy muy cansado... (Energ\u00eda: ${petState.energy.toInt()}%)"
            else -> "\u00a1Necesito atenci\u00f3n!"
        }

        val title = when {
            petState.health <= 0 -> "${petState.name} ha muerto"
            petState.health < 5 -> "${petState.name} est\u00e1 al borde de la muerte"
            petState.hunger < 5 -> "${petState.name} se muere de hambre"
            petState.energy < 5 -> "${petState.name} est\u00e1 en las \u00faltimas"
            else -> "${petState.name} te necesita"
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
            CodeTamagotchiApp.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
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