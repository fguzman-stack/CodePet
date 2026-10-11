package com.tamagotchi.code.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.tamagotchi.code.TestApplication
import com.tamagotchi.code.data.PetOutfit
import com.tamagotchi.code.data.PetWardrobe
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, sdk = [35])
class WardrobeRendererTest {
    private val levels = listOf(1, 5, 10, 25, 50)

    @Test fun allGarmentsAreVisibleAndFitEveryStageInBothModes() {
        for (pixel in listOf(false, true)) for (level in levels) {
            val base = renderCodeyBitmap(level, "HAPPY", width = 160, height = 200, pixelMode = pixel)
            PetWardrobe.items.forEach { item ->
                val outfit = if (item.slot == "HAT") PetOutfit(hat = item.id) else PetOutfit(clothing = item.id)
                val bitmap = renderCodeyBitmap(level, "HAPPY", width = 160, height = 200, pixelMode = pixel, outfit = outfit)
                assertFalse("Invisible ${item.id}/$level/$pixel", bitmap.sameAs(base))
                assertTrue("Clipped ${item.id}/$level/$pixel", (0 until 160).all {
                    Color.alpha(bitmap.getPixel(it, 0)) == 0 && Color.alpha(bitmap.getPixel(it, 199)) == 0
                } && (0 until 200).all {
                    Color.alpha(bitmap.getPixel(0, it)) == 0 && Color.alpha(bitmap.getPixel(159, it)) == 0
                })
                bitmap.recycle()
            }
            base.recycle()
        }
    }

    @Test fun pixelRendererKeepsProportionsAndTransparentMargins() {
        val wide = renderCodeyBitmap(50, "HAPPY", width = 360, height = 190, pixelMode = true)
        val normal = renderCodeyBitmap(50, "HAPPY", width = 180, height = 190, pixelMode = true)
        val crop = Bitmap.createBitmap(wide, 90, 0, 180, 190)
        assertTrue(normal.sameAs(crop))
        wide.recycle(); normal.recycle(); crop.recycle()
    }

    @Test fun exportWardrobeContactSheets() {
        for (pixel in listOf(false, true)) {
            val gallery = Bitmap.createBitmap(180 * 9, 200 * 5, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(gallery)
            canvas.drawColor(Color.rgb(27, 32, 43))
            val text = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 12f }
            levels.forEachIndexed { row, level ->
                (listOf<PetOutfit>(PetOutfit()) + PetWardrobe.items.map {
                    if (it.slot == "HAT") PetOutfit(hat = it.id) else PetOutfit(clothing = it.id)
                }).forEachIndexed { column, outfit ->
                    val left = column * 180f
                    val top = row * 200f
                    canvas.drawText("$level / ${outfit.hat ?: outfit.clothing ?: "base"}", left + 8f, top + 16f, text)
                    val sprite = renderCodeyBitmap(level, "HAPPY", width = 180, height = 180, pixelMode = pixel, outfit = outfit)
                    canvas.drawBitmap(sprite, left, top + 20f, null)
                    sprite.recycle()
                }
            }
            val file = File("build/reports/wardrobe/${if (pixel) "pixel" else "vector"}.png")
            requireNotNull(file.parentFile).mkdirs()
            file.outputStream().use { gallery.compress(Bitmap.CompressFormat.PNG, 100, it) }
            gallery.recycle()
        }
    }
}
