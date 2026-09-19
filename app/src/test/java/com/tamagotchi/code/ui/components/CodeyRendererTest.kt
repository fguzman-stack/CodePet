package com.tamagotchi.code.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.takahirom.roborazzi.captureRoboImage
import com.tamagotchi.code.TestApplication
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, sdk = [35])
class CodeyRendererTest {
    private val levels = listOf(1, 5, 10, 25, 50)

    @Test
    fun allStagesAndMoodsFitSmallAndRectangularWidgets() {
        for (level in levels) for (mood in PetMood.entries) {
            for ((width, height) in listOf(80 to 80, 120 to 80, 80 to 120)) {
                val bitmap = renderCodeyBitmap(level, mood.name, width = width, height = height, time = 17f)
                val pixels = IntArray(width * height)
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                assertTrue("Empty $level/$mood", pixels.any { Color.alpha(it) > 0 })
                assertTrue("Clipped $level/$mood at $width x $height",
                    (0 until width).all { x -> Color.alpha(bitmap.getPixel(x, 0)) == 0 && Color.alpha(bitmap.getPixel(x, height - 1)) == 0 } &&
                        (0 until height).all { y -> Color.alpha(bitmap.getPixel(0, y)) == 0 && Color.alpha(bitmap.getPixel(width - 1, y)) == 0 })
                bitmap.recycle()
            }
        }
    }

    @Test
    fun deathOverridesStatusAndFreezesEveryStageIncludingAntennasAndEgg() {
        for (level in levels) {
            val dead = renderCodeyBitmap(level, "DEAD", time = 0f)
            assertTrue(dead.sameAs(renderCodeyBitmap(level, "DEAD", time = 53f)))
            assertTrue(dead.sameAs(renderCodeyBitmap(level, "EXCITED", isDead = true, time = 132f)))
        }
    }

    @Test
    fun stageMoodAndTimeChangeTheRenderedRobot() {
        val adult = renderCodeyBitmap(10, "HAPPY")
        assertFalse(adult.sameAs(renderCodeyBitmap(5, "HAPPY")))
        assertFalse(adult.sameAs(renderCodeyBitmap(10, "SICK")))
        assertFalse(adult.sameAs(renderCodeyBitmap(10, "HAPPY", time = 17f)))
    }

    @Test
    fun codeyAppearanceGallery() {
        val cellWidth = 180
        val cellHeight = 200
        val gallery = Bitmap.createBitmap(cellWidth * 8, cellHeight * 5, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(gallery)
        canvas.drawColor(Color.rgb(27, 32, 43))
        val label = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 12f }
        val stages = listOf("Egg", "Child", "Adult", "Veteran", "Legendary")
        levels.forEachIndexed { row, level ->
            PetMood.entries.forEachIndexed { column, mood ->
                val left = (column * cellWidth).toFloat()
                val top = (row * cellHeight).toFloat()
                canvas.drawText("${stages[row]} / ${mood.name}", left + 8f, top + 16f, label)
                val sprite = renderCodeyBitmap(level, mood.name, width = cellWidth, height = 180, time = 17f)
                canvas.drawBitmap(sprite, left, top + 20f, null)
                sprite.recycle()
            }
        }
        gallery.captureRoboImage("src/test/screenshots/codey.png")
    }
}
