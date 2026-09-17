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

        // Only stable ids are persisted; display text is resolved by id in the UI layer
        val ALL_ACHIEVEMENTS = listOf(
            Achievement("primer_build"),
            Achievement("nivel_experto"),
            Achievement("cazador_de_bugs"),
            Achievement("git_sin_panico"),
            Achievement("racha_7"),
            Achievement("racha_30"),
            Achievement("coleccionista"),
            Achievement("completista"),
            Achievement("ahorrador"),
            Achievement("primer_acaricie"),
            Achievement("duermevela"),
            Achievement("limpiador"),
        )
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

    fun getAchievementById(id: String): Achievement? =
        ALL_ACHIEVEMENTS.find { it.id == id }
}

data class Achievement(
    val id: String,
    val name: String = "",
    val description: String = ""
)
