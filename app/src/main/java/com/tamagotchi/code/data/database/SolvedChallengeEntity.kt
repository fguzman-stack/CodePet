package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One successful result per built-in challenge, independent of retries. */
@Entity(tableName = "solved_challenges")
data class SolvedChallengeEntity(
    @PrimaryKey val challengeId: Int,
    val language: String
)
