package com.tamagotchi.code.util

object StatusCalculator {
    fun determineStatus(
        health: Float, hunger: Float, energy: Float,
        isSleeping: Boolean, isStudying: Boolean, isExcited: Boolean = false,
        isDead: Boolean = false
    ): String {
        return when {
            isDead -> "DEAD"
            isExcited -> "EXCITED"
            isStudying -> "STUDYING"
            isSleeping -> "SLEEPING"
            health < 20f -> "SICK"
            hunger < 20f -> "HUNGRY"
            energy < 15f -> "SAD"
            else -> "HAPPY"
        }
    }
}
