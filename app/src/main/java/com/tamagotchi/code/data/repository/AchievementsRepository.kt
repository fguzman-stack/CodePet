package com.tamagotchi.code.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.achievementsDataStore by preferencesDataStore(name = "achievements")

class AchievementsRepository(private val context: Context) {
    companion object {
        private val KEY_UNLOCKED_ACHIEVEMENTS = stringSetPreferencesKey("unlocked_achievements")
    }

    val unlockedAchievements: Flow<Set<String>> = context.achievementsDataStore.data.map { prefs ->
        prefs[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet()
    }

    suspend fun unlockAchievement(achievementId: String) {
        context.achievementsDataStore.edit { prefs ->
            val current = prefs[KEY_UNLOCKED_ACHIEVEMENTS] ?: emptySet()
            if (!current.contains(achievementId)) {
                prefs[KEY_UNLOCKED_ACHIEVEMENTS] = current + achievementId
            }
        }
    }
}
