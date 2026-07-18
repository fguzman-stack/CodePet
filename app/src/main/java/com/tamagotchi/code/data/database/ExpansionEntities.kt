package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code_cards")
data class CodeCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val type: String, // TRIVIA, JOKE, TIP
    val shown: Boolean = false
)

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val targetType: String, // STUDY, SHOP, GAME, PET
    val targetValue: Int,
    val currentValue: Int = 0,
    val rewardBytes: Int = 0,
    val rewardXp: Int = 0,
    val isCompleted: Boolean = false,
    val expiresAt: Long = 0
)

@Entity(tableName = "owned_items")
data class OwnedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: String,
    val type: String, // HAT, SKIN, THEME
    val isEquipped: Boolean = false
)

@Entity(tableName = "language_progress")
data class LanguageProgressEntity(
    @PrimaryKey val languageId: String, // KOTLIN, JS, PHP, PYTHON
    val challengesCompleted: Int = 0,
    val totalChallenges: Int = 0,
    val badgeLevel: Int = 0 // 0: None, 1: Bronze, 2: Silver, 3: Gold
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val activityType: String, // STUDY, SHOP, PLAY, SLEEP, PET
    val description: String
)
