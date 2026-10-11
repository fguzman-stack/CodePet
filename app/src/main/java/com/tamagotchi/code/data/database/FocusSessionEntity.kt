package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val topic: String,
    val plannedDurationMinutes: Int,
    val startedAt: Long,
    val status: String = "RUNNING",
    @ColumnInfo(defaultValue = "1.0") val xpMultiplier: Float = 1f
) {
    val endsAt: Long get() = startedAt + plannedDurationMinutes * 60_000L

    fun remainingSeconds(now: Long): Int =
        ((endsAt - now).coerceIn(0L, plannedDurationMinutes * 60_000L)
            .let { (it + 999L) / 1000L }).toInt()
}
