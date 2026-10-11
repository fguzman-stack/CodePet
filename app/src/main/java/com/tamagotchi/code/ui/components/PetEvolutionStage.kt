package com.tamagotchi.code.ui.components

sealed class PetEvolutionStage {
    object Kitten : PetEvolutionStage()
    object Junior : PetEvolutionStage()
    object Developer : PetEvolutionStage()
    object Senior : PetEvolutionStage()
    object Legendary : PetEvolutionStage()

    companion object {
        fun fromLevel(level: Int): PetEvolutionStage = when {
            level < 5 -> Kitten
            level < 10 -> Junior
            level < 25 -> Developer
            level < 50 -> Senior
            else -> Legendary
        }
    }

    val artIndex: Int get() = when (this) {
        Kitten -> 0
        Junior -> 1
        Developer -> 2
        Senior -> 3
        Legendary -> 4
    }
}
