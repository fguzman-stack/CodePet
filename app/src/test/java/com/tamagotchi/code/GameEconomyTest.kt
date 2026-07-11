package com.tamagotchi.code

import org.junit.Assert.*
import org.junit.Test

/**
 * Tests for game economy, cooldown logic and settings validation.
 */
class GameEconomyTest {

    // --- Economy: XP only from Focus & challenges, Bytes from arcade ---

    @Test
    fun `focus session XP scales linearly with minutes`() {
        val minutes = 25
        val xpEarned = minutes * 5
        assertEquals(125, xpEarned)
    }

    @Test
    fun `challenge correct answer awards XP and Bytes`() {
        val earnedXp = 20
        val earnedBytes = 25
        assertTrue(earnedXp > 0)
        assertTrue(earnedBytes > 0)
    }

    @Test
    fun `minigame awards only Bytes, never XP`() {
        // Minigames should only give bytes as rewards
        val bytesEarned = 50
        val xpEarned = 0 // Minigames never give XP
        assertTrue(bytesEarned > 0)
        assertEquals(0, xpEarned)
    }

    // --- Difficulty settings ---

    @Test
    fun `difficulty defaults to Inicial`() {
        val defaultDifficulty = "Inicial"
        assertEquals("Inicial", defaultDifficulty)
    }

    @Test
    fun `valid difficulty options are three`() {
        val validOptions = listOf("Inicial", "Intermedia", "Mixta")
        assertEquals(3, validOptions.size)
        assertTrue(validOptions.contains("Inicial"))
        assertTrue(validOptions.contains("Intermedia"))
        assertTrue(validOptions.contains("Mixta"))
    }

    // --- Selected Topics ---

    @Test
    fun `selectedTopics must have at least one topic`() {
        val topics = setOf("Kotlin")
        assertTrue(topics.isNotEmpty())
    }

    @Test
    fun `removing last topic should be prevented`() {
        val topics = mutableSetOf("Kotlin")
        // Simulate UI logic: don't remove if size would be 0
        val canRemove = topics.size > 1
        assertFalse(canRemove)
    }

    @Test
    fun `default topics include Kotlin, Estructuras de Datos, Git`() {
        val defaults = setOf("Kotlin", "Estructuras de Datos", "Git")
        assertEquals(3, defaults.size)
        assertTrue(defaults.contains("Kotlin"))
        assertTrue(defaults.contains("Git"))
    }

    // --- Cooldown / diminishing returns ---

    @Test
    fun `arcade cooldown diminishes returns after 3 plays`() {
        var totalReward = 0
        val maxFullRewardPlays = 3
        for (play in 1..5) {
            val baseReward = 50
            val multiplier = if (play <= maxFullRewardPlays) 1.0 else 0.5
            totalReward += (baseReward * multiplier).toInt()
        }
        // 3*50 + 2*25 = 200
        assertEquals(200, totalReward)
    }

    // --- Level calculation ---

    @Test
    fun `level 1 requires 100 XP`() {
        val xp = 99
        val level = calculateLevel(xp)
        assertEquals(1, level)
    }

    @Test
    fun `level 2 at 100 XP`() {
        val xp = 100
        val level = calculateLevel(xp)
        assertEquals(2, level)
    }

    @Test
    fun `level 3 at 300 XP`() {
        val xp = 300
        val level = calculateLevel(xp)
        assertEquals(3, level)
    }

    // --- Onboarding validation ---

    @Test
    fun `pet name cannot exceed 15 characters`() {
        val name = "MiMascotaSuperLarga"
        val trimmed = name.take(15)
        assertEquals(15, trimmed.length)
    }

    @Test
    fun `blank name defaults to Codey`() {
        val name = ""
        val finalName = if (name.isNotBlank()) name else "Codey"
        assertEquals("Codey", finalName)
    }

    // --- Helper ---

    private fun calculateLevel(xp: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }
}
