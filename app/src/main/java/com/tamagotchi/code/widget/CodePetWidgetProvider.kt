package com.tamagotchi.code.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity

class CodePetWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val workRequest = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            petState: PetStateEntity? = null
        ) {
            val views = RemoteViews(context.packageName, R.layout.code_pet_widget)

            if (petState != null) {
                views.setTextViewText(R.id.widget_title, petState.name)
                views.setTextViewText(R.id.widget_level, "Nv.${petState.level}")

                val effectiveStatus = if (petState.isDead) "DEAD" else petState.currentStatus

                val subtitle = when (effectiveStatus) {
                    "HAPPY" -> "\u00a1Todo va genial!"
                    "SLEEPING" -> "Zzz... durmiendo"
                    "SICK" -> "No me siento bien"
                    "HUNGRY" -> "\u00a1Tengo hambre!"
                    "SAD" -> "Estoy triste"
                    "STUDYING" -> "\u00a1Modo estudio!"
                    "EXCITED" -> "\u00a1Muy emocionado!"
                    "DEAD" -> "Descansa en paz..."
                    else -> "\u00a1Todo va genial!"
                }
                views.setTextViewText(R.id.widget_subtitle, subtitle)

                val imageRes = when (effectiveStatus) {
                    "DEAD" -> R.drawable.mascota_dead
                    "SLEEPING" -> R.drawable.mascota_sleeping
                    "SICK" -> R.drawable.mascota_sick
                    "HUNGRY" -> R.drawable.mascota_hungry
                    "SAD" -> R.drawable.mascota_sad
                    "STUDYING" -> R.drawable.mascota_studying
                    "EXCITED" -> R.drawable.mascota_excited
                    else -> R.drawable.mascota_happy
                }
                views.setImageViewResource(R.id.widget_pet_image, imageRes)

                views.setViewVisibility(R.id.heart_1, if (petState.health > 0) View.VISIBLE else View.INVISIBLE)
                views.setViewVisibility(R.id.heart_2, if (petState.health >= 34f) View.VISIBLE else View.INVISIBLE)
                views.setViewVisibility(R.id.heart_3, if (petState.health >= 67f) View.VISIBLE else View.INVISIBLE)

                val energy = petState.energy.toInt().coerceIn(0, 100)
                val hunger = petState.hunger.toInt().coerceIn(0, 100)
                views.setProgressBar(R.id.energy_bar, 100, energy, false)
                views.setProgressBar(R.id.hunger_bar, 100, hunger, false)
                views.setTextViewText(R.id.energy_value, "${energy}%")
                views.setTextViewText(R.id.hunger_value, "${hunger}%")
                views.setTextViewText(R.id.widget_bytes, "${petState.bytes} bytes")
                views.setTextViewText(R.id.widget_streak, "Racha: ${petState.streak} d\u00edas")
            } else {
                views.setTextViewText(R.id.widget_title, "CodePet")
                views.setTextViewText(R.id.widget_level, "Nv.1")
                views.setTextViewText(R.id.widget_subtitle, "\u00a1Inicia la app!")
                views.setImageViewResource(R.id.widget_pet_image, R.drawable.mascota_happy)
                views.setProgressBar(R.id.energy_bar, 100, 0, false)
                views.setProgressBar(R.id.hunger_bar, 100, 0, false)
                views.setTextViewText(R.id.energy_value, "0%")
                views.setTextViewText(R.id.hunger_value, "0%")
                views.setTextViewText(R.id.widget_bytes, "0 bytes")
                views.setTextViewText(R.id.widget_streak, "Racha: 0 d\u00edas")
            }

            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
