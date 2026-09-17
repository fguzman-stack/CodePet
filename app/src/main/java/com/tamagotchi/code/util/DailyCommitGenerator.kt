package com.tamagotchi.code.util

import android.content.Context
import androidx.annotation.StringRes
import com.tamagotchi.code.R
import kotlin.random.Random

object DailyCommitGenerator {

    fun generateCommit(context: Context, activities: Set<String>, petName: String): String {
        if (activities.isEmpty()) {
            return randomOf(
                context, petName,
                R.string.commit_idle_1, R.string.commit_idle_2, R.string.commit_idle_3,
                R.string.commit_idle_4, R.string.commit_idle_5
            )
        }

        val hasStudied = activities.any { it.contains("study", ignoreCase = true) }
        val hasShopped = activities.any { it.contains("shop", ignoreCase = true) || it.contains("buy", ignoreCase = true) }
        val hasPlayed = activities.any { it.contains("play", ignoreCase = true) || it.contains("game", ignoreCase = true) }
        val hasSlept = activities.any { it.contains("sleep", ignoreCase = true) }
        val hasPetted = activities.any { it.contains("pet", ignoreCase = true) || it.contains("acarici", ignoreCase = true) }
        val hasClean = activities.any { it.contains("clean", ignoreCase = true) || it.contains("limpi", ignoreCase = true) }
        val hasChallenge = activities.any { it.contains("challenge", ignoreCase = true) || it.contains("reto", ignoreCase = true) }

        val activeCount = listOf(hasStudied, hasShopped, hasPlayed, hasSlept, hasPetted, hasClean, hasChallenge).count { it }

        return when {
            activeCount >= 3 -> randomOf(
                context, petName,
                R.string.commit_busy_1, R.string.commit_busy_2, R.string.commit_busy_3
            )
            hasStudied && hasPetted -> context.getString(R.string.commit_study_love)
            hasStudied -> randomOf(
                context, petName,
                R.string.commit_study_1, R.string.commit_study_2, R.string.commit_study_3
            )
            hasShopped -> randomOf(
                context, petName,
                R.string.commit_shop_1, R.string.commit_shop_2, R.string.commit_shop_3
            )
            hasPlayed -> randomOf(
                context, petName,
                R.string.commit_play_1, R.string.commit_play_2, R.string.commit_play_3
            )
            hasSlept -> randomOf(
                context, petName,
                R.string.commit_sleep_1, R.string.commit_sleep_2, R.string.commit_sleep_3
            )
            hasPetted -> randomOf(
                context, petName,
                R.string.commit_pet_1, R.string.commit_pet_2, R.string.commit_pet_3
            )
            hasClean -> context.getString(R.string.commit_clean_1)
            hasChallenge -> randomOf(
                context, petName,
                R.string.commit_challenge_1, R.string.commit_challenge_2, R.string.commit_challenge_3
            )
            else -> randomOf(
                context, petName,
                R.string.commit_normal_1, R.string.commit_normal_2, R.string.commit_normal_3
            )
        }
    }

    private fun randomOf(context: Context, petName: String, @StringRes vararg messages: Int): String {
        return context.getString(messages[Random.nextInt(messages.size)], petName)
    }
}
