package com.tamagotchi.code.ui.components

object PetAnimationConfig {
    fun petFloatDurationMs(status: String, reduceMotion: Boolean): Int = when {
        reduceMotion -> 2400
        status == "EXCITED" -> 1300
        status == "HAPPY" -> 1600
        status == "SLEEPING" -> 3200
        else -> 2200
    }

    fun petFloatAmplitudeDp(status: String): Float = when (status) {
        "EXCITED" -> 8f
        "HAPPY" -> 6f
        "SLEEPING" -> 4f
        else -> 5f
    }

    fun bounceDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 220 else 420

    fun heartDurationMs(reduceMotion: Boolean): Int = if (reduceMotion) 900 else 1500
}
