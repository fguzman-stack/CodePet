package com.tamagotchi.code.ui.components

import org.junit.Assert.assertTrue
import org.junit.Test

class PetAnimationConfigTest {
    @Test
    fun reduceMotionUsesCalmerDurations() {
        assertTrue(PetAnimationConfig.petFloatDurationMs("EXCITED", false) > PetAnimationConfig.petFloatDurationMs("EXCITED", true))
        assertTrue(PetAnimationConfig.bounceDurationMs(false) > PetAnimationConfig.bounceDurationMs(true))
        assertTrue(PetAnimationConfig.heartDurationMs(false) > PetAnimationConfig.heartDurationMs(true))
    }
}
