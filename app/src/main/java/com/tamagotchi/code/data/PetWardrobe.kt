package com.tamagotchi.code.data

import androidx.annotation.StringRes
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.OwnedItemEntity

data class PetCosmetic(
    val id: String,
    val slot: String,
    @param:StringRes val nameRes: Int,
    val cost: Int = 0,
    val language: String? = null
)

object PetWardrobe {
    val items = listOf(
        PetCosmetic("beanie", "HAT", R.string.cosmetic_beanie, 80),
        PetCosmetic("top_hat", "HAT", R.string.cosmetic_top_hat, 140),
        PetCosmetic("scarf", "CLOTHING", R.string.cosmetic_scarf, 60),
        PetCosmetic("dev_jacket", "CLOTHING", R.string.cosmetic_dev_jacket, 160),
        PetCosmetic("kotlin_beret", "HAT", R.string.cosmetic_kotlin_beret, language = "Kotlin"),
        PetCosmetic("js_jacket", "CLOTHING", R.string.cosmetic_js_jacket, language = "JavaScript"),
        PetCosmetic("php_robe", "CLOTHING", R.string.cosmetic_php_robe, language = "PHP"),
        PetCosmetic("python_hood", "HAT", R.string.cosmetic_python_hood, language = "Python")
    )

    fun find(id: String) = items.find { it.id == id }
}

data class PetOutfit(val hat: String? = null, val clothing: String? = null) {
    companion object {
        fun from(items: List<OwnedItemEntity>) = PetOutfit(
            items.firstOrNull { it.type == "HAT" && it.isEquipped }?.itemId,
            items.firstOrNull { it.type == "CLOTHING" && it.isEquipped }?.itemId
        )
    }
}
