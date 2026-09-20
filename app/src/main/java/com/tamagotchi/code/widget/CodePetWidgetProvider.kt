package com.tamagotchi.code.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tamagotchi.code.MainActivity
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.components.renderCodeyBitmap

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
            petState: PetStateEntity? = null,
            pixelMode: Boolean = false
        ) {
            appWidgetManager.updateAppWidget(appWidgetId, createRemoteViews(context, petState, pixelMode))
        }

        internal fun createRemoteViews(
            context: Context,
            petState: PetStateEntity?,
            pixelMode: Boolean = false
        ): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.code_pet_widget)

            views.setTextViewText(R.id.widget_title, petState?.name ?: context.getString(R.string.default_pet_name))
            views.setTextViewText(R.id.widget_level, context.getString(R.string.widget_level, petState?.level ?: 1))

            val effectiveStatus = when {
                petState == null -> null
                petState.isDead -> "DEAD"
                else -> petState.currentStatus
            }
            val subtitleRes = when (effectiveStatus) {
                null -> R.string.widget_start_app
                "HAPPY" -> R.string.widget_status_happy
                "SLEEPING" -> R.string.widget_status_sleeping
                "SICK" -> R.string.widget_status_sick
                "HUNGRY" -> R.string.widget_status_hungry
                "SAD" -> R.string.widget_status_sad
                "STUDYING" -> R.string.widget_status_studying
                "EXCITED" -> R.string.widget_status_excited
                "DEAD" -> R.string.widget_status_dead
                else -> R.string.widget_status_happy
            }
            views.setTextViewText(R.id.widget_subtitle, context.getString(subtitleRes))

            views.setImageViewBitmap(
                R.id.widget_pet_image,
                renderCodeyBitmap(
                    petState?.level ?: 1,
                    effectiveStatus ?: "HAPPY",
                    petState?.isDead ?: false,
                    pixelMode = pixelMode
                )
            )

            val health = (petState?.health ?: 0f).toInt().coerceIn(0, 100)
            val energy = (petState?.energy ?: 0f).toInt().coerceIn(0, 100)
            val hunger = (petState?.hunger ?: 0f).toInt().coerceIn(0, 100)
            views.setProgressBar(R.id.health_bar, 100, health, false)
            views.setProgressBar(R.id.energy_bar, 100, energy, false)
            views.setProgressBar(R.id.hunger_bar, 100, hunger, false)
            views.setTextViewText(R.id.health_value, "${health}%")
            views.setTextViewText(R.id.energy_value, "${energy}%")
            views.setTextViewText(R.id.hunger_value, "${hunger}%")

            views.setTextViewText(R.id.widget_bytes, context.getString(R.string.widget_bytes, petState?.bytes ?: 0))
            val streak = petState?.streak ?: 0
            views.setTextViewText(
                R.id.widget_streak,
                context.resources.getQuantityString(R.plurals.widget_streak, streak, streak)
            )

            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            return views
        }
    }
}
