package com.tamagotchi.code.data.repository

import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity

/** The persisted result of one RUNNING -> COMPLETED transition. */
data class FocusSessionCompletion(
    val session: FocusSessionEntity,
    val pet: PetStateEntity,
    val xp: Int,
    val bytes: Int
)
