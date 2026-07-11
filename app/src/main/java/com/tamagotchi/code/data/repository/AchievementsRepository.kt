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

        val ALL_ACHIEVEMENTS = listOf(
            Achievement("primer_build", "Primer build", "Completá tu primera sesión Focus"),
            Achievement("nivel_experto", "Nivel Experto", "Subí de nivel por primera vez"),
            Achievement("cazador_de_bugs", "Cazador de bugs", "Puntuación perfecta en Bug Hunt"),
            Achievement("git_sin_panico", "Git sin pánico", "Aprobá Git Rescue con 3+ aciertos"),
            Achievement("racha_7", "Racha de 7 días", "Mantené una racha de estudio de 7 días"),
            Achievement("racha_30", "Racha de 30 días", "Mantené una racha de estudio de 30 días"),
            Achievement("coleccionista", "Coleccionista", "Desbloqueá 3 temas visuales"),
            Achievement("completista", "Completista", "Desbloqueá los 12 temas visuales"),
            Achievement("ahorrador", "Ahorrador", "Acumulá 1000 Bytes"),
            Achievement("primer_acaricie", "Primer mimo", "Acariciá a tu mascota por primera vez"),
            Achievement("duermevela", "Duermevela", "Poné a dormir a tu mascota"),
            Achievement("limpiador", "Limpieza profunda", "Limpiá a tu mascota 10 veces"),
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
    val name: String,
    val description: String
)
