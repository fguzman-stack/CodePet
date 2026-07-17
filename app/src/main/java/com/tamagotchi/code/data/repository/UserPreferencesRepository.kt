package com.tamagotchi.code.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tamagotchi.code.ui.theme.ThemeRegistry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val KEY_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
        private val KEY_THEME = stringPreferencesKey("app_theme")
        private val KEY_UNLOCKED_THEMES = stringSetPreferencesKey("unlocked_themes")
        private val KEY_SELECTED_TOPICS = stringSetPreferencesKey("selected_topics")
        private val KEY_DIFFICULTY = stringPreferencesKey("difficulty")
        private val KEY_FOCUS_DURATION = androidx.datastore.preferences.core.intPreferencesKey("focus_duration_default")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        private val KEY_VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val KEY_REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        private val KEY_GAME_LAST_PLAYED = stringSetPreferencesKey("game_last_played")
        private val KEY_DEFAULT_THEME_MODE = stringPreferencesKey("default_theme_mode")
        val KEY_LAST_DAILY_REWARD_CLAIM_TIME = androidx.datastore.preferences.core.longPreferencesKey("last_daily_reward_claim_time")
        val KEY_DAILY_REWARD_DAY = androidx.datastore.preferences.core.intPreferencesKey("daily_reward_day")
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING] ?: false
    }

    val currentTheme: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_THEME] ?: "Default"
    }

    val defaultThemeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_THEME_MODE] ?: "SYSTEM"
    }

    val unlockedThemes: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        val stored = prefs[KEY_UNLOCKED_THEMES]
        val allNames = ThemeRegistry.allThemes.map { it.name }.toSet()
        if (stored == null || stored.isEmpty()) allNames else stored
    }

    val selectedTopics: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_SELECTED_TOPICS] ?: setOf("Kotlin", "Estructuras de Datos", "Git")
    }

    val difficulty: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_DIFFICULTY] ?: "Inicial"
    }

    val focusDurationDefault: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_FOCUS_DURATION] ?: 25
    }

    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SOUND_ENABLED] ?: true
    }

    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_VIBRATION_ENABLED] ?: true
    }

    val reduceMotion: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_REDUCE_MOTION] ?: false
    }

    val gameLastPlayed: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
    }

    val gameCooldowns: Flow<Map<String, Long>> = context.dataStore.data.map { prefs ->
        val raw = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
        raw.mapNotNull { entry ->
            val parts = entry.split(":", limit = 2)
            if (parts.size == 2) {
                val timestamp = parts[1].toLongOrNull()
                if (timestamp != null) parts[0] to timestamp else null
            } else null
        }.toMap()
    }

    val lastDailyRewardClaimTime: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[KEY_LAST_DAILY_REWARD_CLAIM_TIME] ?: 0L
    }

    val dailyRewardDay: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_DAILY_REWARD_DAY] ?: 0
    }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING] = true
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = theme
        }
    }

    suspend fun addUnlockedTheme(theme: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_UNLOCKED_THEMES] ?: ThemeRegistry.allThemes.map { it.name }.toSet()
            prefs[KEY_UNLOCKED_THEMES] = current + theme
        }
    }

    suspend fun setUnlockedThemes(themes: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_UNLOCKED_THEMES] = themes
        }
    }

    suspend fun setDefaultThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DEFAULT_THEME_MODE] = mode
        }
    }

    suspend fun getHasSeenOnboarding(): Boolean {
        return context.dataStore.data.first()[KEY_ONBOARDING] ?: false
    }

    suspend fun setSelectedTopics(topics: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_TOPICS] = topics
        }
    }

    suspend fun setDifficulty(difficulty: String) {
        context.dataStore.edit { prefs -> prefs[KEY_DIFFICULTY] = difficulty }
    }

    suspend fun setFocusDurationDefault(duration: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_FOCUS_DURATION] = duration }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_VIBRATION_ENABLED] = enabled }
    }

    suspend fun setReduceMotion(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_REDUCE_MOTION] = enabled }
    }

    suspend fun recordGamePlay(gameId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
            val now = System.currentTimeMillis()
            val filtered = current.filter { !it.startsWith("$gameId:") }.toSet()
            prefs[KEY_GAME_LAST_PLAYED] = filtered + "$gameId:$now"
        }
    }

    suspend fun getGameLastPlayed(gameId: String): Long {
        return context.dataStore.data.first().let { prefs ->
            val raw = prefs[KEY_GAME_LAST_PLAYED] ?: emptySet()
            raw.firstOrNull { it.startsWith("$gameId:") }?.split(":")?.get(1)?.toLongOrNull() ?: 0L
        }
    }

    suspend fun setDailyRewardClaim(day: Int, time: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DAILY_REWARD_DAY] = day
            prefs[KEY_LAST_DAILY_REWARD_CLAIM_TIME] = time
        }
    }
}
