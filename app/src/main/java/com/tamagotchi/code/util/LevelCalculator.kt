package com.tamagotchi.code.util

object LevelCalculator {
    fun calculateLevel(xp: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }

    fun xpForNextLevel(level: Int) = level * 100
}
