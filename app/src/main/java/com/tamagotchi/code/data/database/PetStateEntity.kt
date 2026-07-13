package com.tamagotchi.code.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_state")
data class PetStateEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Codey",
    val language: String = "Kotlin",
    val level: Int = 1,
    val xp: Int = 0,
    val hunger: Float = 100f,
    val health: Float = 100f,
    val energy: Float = 100f,
    val bytes: Int = 50,
    val lastUpdated: Long = System.currentTimeMillis(),
    val streak: Int = 0,
    val lastStudyDate: Long = 0,
    val currentStatus: String = "HAPPY",
    val hasRenamed: Boolean = false
)
