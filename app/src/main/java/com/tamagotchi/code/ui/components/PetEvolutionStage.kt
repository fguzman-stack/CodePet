package com.tamagotchi.code.ui.components

sealed class PetEvolutionStage {
    object Egg : PetEvolutionStage()
    object Child : PetEvolutionStage()
    object Adult : PetEvolutionStage()
    object Veteran : PetEvolutionStage()
    object Legendary : PetEvolutionStage()

    companion object {
        fun fromLevel(level: Int): PetEvolutionStage = when {
            level < 5 -> Egg
            level < 10 -> Child
            level < 25 -> Adult
            level < 50 -> Veteran
            else -> Legendary
        }
    }
}
