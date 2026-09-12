package com.tamagotchi.code

import com.tamagotchi.code.util.StatusCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class StatusCalculatorTest {
  @Test
  fun `dead pet always reports DEAD regardless of stats`() {
    assertEquals(
      "DEAD",
      StatusCalculator.determineStatus(
        health = 0f, hunger = 0f, energy = 0f,
        isSleeping = false, isStudying = false, isDead = true
      )
    )
  }

  @Test
  fun `dead wins over sleeping and studying`() {
    assertEquals(
      "DEAD",
      StatusCalculator.determineStatus(
        health = 50f, hunger = 50f, energy = 50f,
        isSleeping = true, isStudying = true, isDead = true
      )
    )
  }

  @Test
  fun `alive pet keeps normal status resolution`() {
    assertEquals(
      "HUNGRY",
      StatusCalculator.determineStatus(
        health = 80f, hunger = 10f, energy = 80f,
        isSleeping = false, isStudying = false, isDead = false
      )
    )
    assertEquals(
      "HAPPY",
      StatusCalculator.determineStatus(
        health = 90f, hunger = 90f, energy = 90f,
        isSleeping = false, isStudying = false, isDead = false
      )
    )
  }
}
