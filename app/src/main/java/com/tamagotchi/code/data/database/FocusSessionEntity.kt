package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val plannedDurationMinutes: Int,
    val startedAt: Long,
    val status: String = "RUNNING"
)
