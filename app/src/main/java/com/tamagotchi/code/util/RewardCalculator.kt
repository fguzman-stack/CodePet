package com.tamagotchi.code.util

import com.tamagotchi.code.data.database.PetStateEntity
import java.util.Calendar

object RewardCalculator {
    data class StudyReward(
        val bytes: Int, val xp: Int, val energyCost: Float,
        val streak: Int, val newLevel: Int
    )

    data class ChallengeReward(
        val bytes: Int, val xp: Int,
        val hungerRestore: Float, val healthRestore: Float
    )

    fun calculateStudyReward(
        minutes: Int, currentXp: Int, currentLevel: Int, currentStreak: Int, lastStudyDate: Long
    ): StudyReward {
        val baseBytes = minutes * 2
        val baseXP = minutes * 3
        val bonusBytes = if (minutes >= 25) 50 else 0
        val bonusXP = if (minutes >= 25) 75 else 0

        val totalBytes = baseBytes + bonusBytes
        val totalXp = baseXP + bonusXP
        val energyCost = (minutes * 0.5f).coerceAtMost(30f)

        val now = System.currentTimeMillis()
        var newStreak = currentStreak

        if (lastStudyDate == 0L) {
            newStreak = 1
        } else {
            val lastCal = Calendar.getInstance().apply { timeInMillis = lastStudyDate }
            val nowCal = Calendar.getInstance().apply { timeInMillis = now }

            val sameDay = lastCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
                    lastCal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

            if (!sameDay) {
                val yesterdayCal = Calendar.getInstance().apply {
                    timeInMillis = now
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                val studiedYesterday = lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                        lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)

                newStreak = if (studiedYesterday) currentStreak + 1 else 1
            }
        }

        val newLevel = LevelCalculator.calculateLevel(currentXp + totalXp)

        return StudyReward(bytes = totalBytes, xp = totalXp, energyCost = energyCost, streak = newStreak, newLevel = newLevel)
    }

    fun calculateChallengeReward(type: String): ChallengeReward {
        val bytes = if (type == "DEBUG") 30 else 25
        val xp = if (type == "DEBUG") 25 else 20
        return ChallengeReward(bytes = bytes, xp = xp, hungerRestore = 15f, healthRestore = 20f)
    }

    fun calculateMinigameReward(score: Int, maxBytes: Int): Int {
        return (score * 10).coerceAtMost(maxBytes)
    }

    fun calculateRefactorReward(attempts: Int): Int {
        return maxOf(50 - attempts * 5, 10)
    }
}
