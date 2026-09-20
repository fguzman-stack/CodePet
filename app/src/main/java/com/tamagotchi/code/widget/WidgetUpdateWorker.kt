package com.tamagotchi.code.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.java.KoinJavaComponent.get

class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = get<AppDatabase>(AppDatabase::class.java).petDao()
        val petState = dao.getPetStateSuspend()
        val themeName = runCatching {
            get<UserPreferencesRepository>(UserPreferencesRepository::class.java).currentTheme.firstOrNull()
        }.getOrNull()
        val pixelMode = themeName == "Retro Pixel"

        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(applicationContext, CodePetWidgetProvider::class.java)
        )

        for (widgetId in widgetIds) {
            CodePetWidgetProvider.updateAppWidget(
                applicationContext,
                appWidgetManager,
                widgetId,
                petState
            )
        }

        return Result.success()
    }
}
