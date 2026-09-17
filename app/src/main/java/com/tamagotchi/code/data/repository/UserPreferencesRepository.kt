package com.tamagotchi.code.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.util.DifficultyKey
import com.tamagotchi.code.util.TopicKey
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
        private val KEY_DAILY_ACTIVITY_LOG = stringSetPreferencesKey("daily_activity_log")
        private val KEY_LAST_COMMIT_DATE = stringPreferencesKey("last_commit_date")
        private val KEY_PENDING_COMMIT = stringPreferencesKey("pending_commit")
        private val KEY_SHOWN_CARDS = stringSetPreferencesKey("shown_cards")
        private val KEY_ACTIVE_QUEST_TYPE = stringPreferencesKey("active_quest_type")
        private val KEY_ACTIVE_QUEST_PROGRESS = androidx.datastore.preferences.core.intPreferencesKey("active_quest_progress")
        private val KEY_ACTIVE_QUEST_EXPIRES = androidx.datastore.preferences.core.longPreferencesKey("active_quest_expires")
        private val KEY_ACTIVE_QUEST_STARTED = androidx.datastore.preferences.core.longPreferencesKey("active_quest_started")
        private val KEY_PET_ACCENT_COLOR = androidx.datastore.preferences.core.intPreferencesKey("pet_accent_color")
        private val KEY_MOODLET_TYPE = stringPreferencesKey("moodlet_type")
        private val KEY_MOODLET_EXPIRY = androidx.datastore.preferences.core.longPreferencesKey("moodlet_expiry")
        private val KEY_LAST_MOODLET_CHECK = androidx.datastore.preferences.core.longPreferencesKey("last_moodlet_check")
        private val KEY_SKILL_TREE = stringSetPreferencesKey("skill_tree_data")
        private val KEY_SEASON_PASS_LEVEL = androidx.datastore.preferences.core.intPreferencesKey("season_pass_level")
        private val KEY_SEASON_PASS_XP = androidx.datastore.preferences.core.intPreferencesKey("season_pass_xp")
        private val KEY_SEASON_PASS_PREMIUM = booleanPreferencesKey("season_pass_premium")
        private val KEY_WEEKLY_MISSIONS = stringSetPreferencesKey("weekly_missions")
        private val KEY_HACKATHON = stringPreferencesKey("hackathon_data")
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
        (prefs[KEY_SELECTED_TOPICS] ?: setOf("Kotlin", "Estructuras de Datos", "Git"))
            .map { TopicKey.normalize(it) }.toSet()
    }

    val difficulty: Flow<String> = context.dataStore.data.map { prefs ->
        DifficultyKey.normalize(prefs[KEY_DIFFICULTY] ?: "Inicial")
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

    suspend fun addDailyActivity(activity: String) {
        context.dataStore.edit { prefs ->
            val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
            val storedDate = prefs[KEY_LAST_COMMIT_DATE]
            if (storedDate != today) {
                prefs[KEY_DAILY_ACTIVITY_LOG] = setOf(activity)
                prefs[KEY_LAST_COMMIT_DATE] = today
            } else {
                val current = prefs[KEY_DAILY_ACTIVITY_LOG] ?: emptySet()
                prefs[KEY_DAILY_ACTIVITY_LOG] = current + activity
            }
        }
    }

    suspend fun getDailyActivityLog(): Set<String> {
        return context.dataStore.data.first()[KEY_DAILY_ACTIVITY_LOG] ?: emptySet()
    }

    suspend fun clearDailyActivityLog() {
        context.dataStore.edit { prefs ->
            prefs[KEY_DAILY_ACTIVITY_LOG] = emptySet()
        }
    }

    suspend fun setPendingCommit(commit: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PENDING_COMMIT] = commit
        }
    }

    suspend fun getPendingCommit(): String? {
        return context.dataStore.data.first()[KEY_PENDING_COMMIT]
    }

    suspend fun clearPendingCommit() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_PENDING_COMMIT)
        }
    }

    val pendingCommitFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_PENDING_COMMIT]
    }

    suspend fun getShownCards(): Set<String> {
        return context.dataStore.data.first()[KEY_SHOWN_CARDS] ?: emptySet()
    }

    suspend fun markCardShown(cardId: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_SHOWN_CARDS] ?: emptySet()
            prefs[KEY_SHOWN_CARDS] = current + cardId.toString()
        }
    }

    suspend fun resetShownCards() {
        context.dataStore.edit { prefs ->
            prefs[KEY_SHOWN_CARDS] = emptySet()
        }
    }

    suspend fun setActiveQuest(type: String, progress: Int, expiresAt: Long, startedAt: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACTIVE_QUEST_TYPE] = type
            prefs[KEY_ACTIVE_QUEST_PROGRESS] = progress
            prefs[KEY_ACTIVE_QUEST_EXPIRES] = expiresAt
            prefs[KEY_ACTIVE_QUEST_STARTED] = startedAt
        }
    }

    suspend fun getActiveQuestType(): String? {
        return context.dataStore.data.first()[KEY_ACTIVE_QUEST_TYPE]
    }

    suspend fun getActiveQuestProgress(): Int {
        return context.dataStore.data.first()[KEY_ACTIVE_QUEST_PROGRESS] ?: 0
    }

    suspend fun getActiveQuestExpires(): Long {
        return context.dataStore.data.first()[KEY_ACTIVE_QUEST_EXPIRES] ?: 0L
    }

    suspend fun getActiveQuestStarted(): Long {
        return context.dataStore.data.first()[KEY_ACTIVE_QUEST_STARTED] ?: 0L
    }

    suspend fun incrementQuestProgress() {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_ACTIVE_QUEST_PROGRESS] ?: 0
            prefs[KEY_ACTIVE_QUEST_PROGRESS] = current + 1
        }
    }

    suspend fun clearActiveQuest() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_ACTIVE_QUEST_TYPE)
            prefs.remove(KEY_ACTIVE_QUEST_PROGRESS)
            prefs.remove(KEY_ACTIVE_QUEST_EXPIRES)
            prefs.remove(KEY_ACTIVE_QUEST_STARTED)
        }
    }

    // === PET ACCENT COLOR ===
    suspend fun setPetAccentColor(color: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_PET_ACCENT_COLOR] = color }
    }

    suspend fun getPetAccentColor(): Int {
        return context.dataStore.data.first()[KEY_PET_ACCENT_COLOR] ?: 0
    }

    // === MOODLET ===
    data class MoodletData(val moodletType: String, val expiry: Long)

    suspend fun saveMoodlet(type: String, expiry: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MOODLET_TYPE] = type
            prefs[KEY_MOODLET_EXPIRY] = expiry
        }
    }

    suspend fun getMoodletData(): MoodletData? {
        val prefs = context.dataStore.data.first()
        val type = prefs[KEY_MOODLET_TYPE] ?: return null
        val expiry = prefs[KEY_MOODLET_EXPIRY] ?: return null
        return MoodletData(type, expiry)
    }

    suspend fun clearMoodlet() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_MOODLET_TYPE)
            prefs.remove(KEY_MOODLET_EXPIRY)
        }
    }

    suspend fun getLastMoodletCheck(): Long {
        return context.dataStore.data.first()[KEY_LAST_MOODLET_CHECK] ?: 0L
    }

    suspend fun setLastMoodletCheck(time: Long) {
        context.dataStore.edit { prefs -> prefs[KEY_LAST_MOODLET_CHECK] = time }
    }

    // === SKILL TREE ===
    suspend fun getSkillTreeData(): List<com.tamagotchi.code.ui.viewmodel.SkillNodeData> {
        val raw = context.dataStore.data.first()[KEY_SKILL_TREE] ?: return emptyList()
        return raw.mapNotNull { entry ->
            val parts = entry.split(":", limit = 4)
            if (parts.size == 4) {
                com.tamagotchi.code.ui.viewmodel.SkillNodeData(
                    id = parts[0], name = parts[1], description = parts[2],
                    currentTier = parts[3].toIntOrNull() ?: 0
                )
            } else null
        }
    }

    suspend fun saveSkillTreeData(skills: List<com.tamagotchi.code.ui.viewmodel.SkillNodeData>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SKILL_TREE] = skills.map { "${it.id}:${it.name}:${it.description}:${it.currentTier}" }.toSet()
        }
    }

    // === SEASON PASS ===
    suspend fun getSeasonPassData(): com.tamagotchi.code.ui.viewmodel.SeasonPassData? {
        val prefs = context.dataStore.data.first()
        val level = prefs[KEY_SEASON_PASS_LEVEL] ?: return null
        val xp = prefs[KEY_SEASON_PASS_XP] ?: 0
        val premium = prefs[KEY_SEASON_PASS_PREMIUM] ?: false
        return com.tamagotchi.code.ui.viewmodel.SeasonPassData(level, xp, premium)
    }

    suspend fun saveSeasonPassData(data: com.tamagotchi.code.ui.viewmodel.SeasonPassData) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SEASON_PASS_LEVEL] = data.level
            prefs[KEY_SEASON_PASS_XP] = data.xp
            prefs[KEY_SEASON_PASS_PREMIUM] = data.premium
        }
    }

    // === WEEKLY MISSIONS ===
    suspend fun getWeeklyMissions(): List<com.tamagotchi.code.ui.viewmodel.WeeklyMissionData> {
        val raw = context.dataStore.data.first()[KEY_WEEKLY_MISSIONS] ?: return emptyList()
        return raw.mapNotNull { entry ->
            val parts = entry.split("|", limit = 6)
            if (parts.size == 6) {
                com.tamagotchi.code.ui.viewmodel.WeeklyMissionData(
                    id = parts[0], title = parts[1], description = parts[2],
                    rewardXp = parts[3].toIntOrNull() ?: 0,
                    rewardBytes = parts[4].toIntOrNull() ?: 0,
                    completed = parts[5].toBoolean()
                )
            } else null
        }
    }

    suspend fun saveWeeklyMissions(missions: List<com.tamagotchi.code.ui.viewmodel.WeeklyMissionData>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_WEEKLY_MISSIONS] = missions.map {
                "${it.id}|${it.title}|${it.description}|${it.rewardXp}|${it.rewardBytes}|${it.completed}"
            }.toSet()
        }
    }

    // === HACKATHON ===
    suspend fun getHackathonData(): com.tamagotchi.code.ui.viewmodel.HackathonData? {
        val raw = context.dataStore.data.first()[KEY_HACKATHON] ?: return null
        val parts = raw.split("|", limit = 4)
        if (parts.size == 4) {
            return com.tamagotchi.code.ui.viewmodel.HackathonData(
                active = parts[0].toBoolean(),
                attempts = parts[1].toIntOrNull() ?: 0,
                bestTimeMs = parts[2].toLongOrNull() ?: Long.MAX_VALUE,
                expiresAt = parts[3].toLongOrNull() ?: System.currentTimeMillis()
            )
        }
        return null
    }

    suspend fun saveHackathonData(data: com.tamagotchi.code.ui.viewmodel.HackathonData) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HACKATHON] = "${data.active}|${data.attempts}|${data.bestTimeMs}|${data.expiresAt}"
        }
    }
}
