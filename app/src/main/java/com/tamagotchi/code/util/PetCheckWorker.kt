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
            petState.health <= 0 -> "${petState.name} ha muerto"
            petState.health < 5 -> "${petState.name} está al borde de la muerte"
            petState.hunger < 5 -> "${petState.name} se muere de hambre"
            petState.energy < 5 -> "${petState.name} está en las últimas"
            else -> "${petState.name} te necesita"
        }

        val reason = when {
            petState.health <= 0f -> "Me he ido al otro lado del compilador... ¿me rescatas?"
            petState.health < 5f -> "Me duele hasta el último bit... ¿puedes revisarme? (Salud: ${petState.health.toInt()}%)"
            petState.health < 20f -> "Noto que el Blue Screen of Death se acerca... (Salud: ${petState.health.toInt()}%)"
            petState.hunger < 5f -> "Llevo horas sin comer, ¿crees que soy un microservicio? (Hambre: ${petState.hunger.toInt()}%)"
            petState.hunger < 20f -> "Mi estómago está haciendo un loop infinito de ruidos. (Hambre: ${petState.hunger.toInt()}%)"
            petState.energy < 5f -> "Mi batería está en rojo. Y no es una metáfora de código. (Energía: ${petState.energy.toInt()}%)"
            petState.energy < 15f -> "Oye, ¿has visto mis logs? Están llenos de NullPointerException existenciales. (Energía: ${petState.energy.toInt()}%)"
            else -> "¡Necesito atención!"
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