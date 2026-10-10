package com.tamagotchi.code.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.tamagotchi.code.TestApplication
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.theme.contrastRatio
import java.io.File
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
class ScenicThemeRendererTest {
    private val themes = mapOf(ScenicTheme.OCEAN to "Océano", ScenicTheme.JAPANESE to "Japonés", ScenicTheme.AURORA to "Aurora")

    private fun frame(scene: ScenicTheme, phase: Float, width: Int = 360, height: Int = 720): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, ComposeCanvas(bitmap.asImageBitmap()),
            Size(width.toFloat(), height.toFloat())) { drawScenicFrame(scene, phase) }
        return bitmap
    }

    @Test
    fun scenesAnimateAndKeepTheCentralContentAreaReadableAtPhoneAndLandscapeSizes() {
        for ((scene, themeName) in themes) {
            val still = frame(scene, 0f)
            val moving = frame(scene, 0.125f)
            assertFalse("$scene is static", still.sameAs(moving))
            still.recycle(); moving.recycle()
            for ((width, height) in listOf(360 to 720, 720 to 360)) {
                for (phase in listOf(0f, 0.125f, 0.5f, 0.999f)) {
                    val bitmap = frame(scene, phase, width, height)
                    val foreground = ThemeRegistry.getTheme(themeName).textPrimary
                    for (x in width / 4..width * 3 / 4 step 12) {
                        for (y in height / 3..height * 4 / 5 step 12) {
                            val background = Color(bitmap.getPixel(x, y))
                            assertTrue("$scene/$phase has transparent holes", background.alpha == 1f)
                            assertTrue("$scene/$phase obscures content at $x,$y", contrastRatio(foreground, background) >= 4.5f)
                        }
                    }
                    bitmap.recycle()
                }
            }
        }
    }

    @Test
    fun eggHasDistinctMoodExpressionsAndRemainsAnimated() {
        val happy = renderCodeyBitmap(1, "HAPPY")
        for (mood in listOf("SLEEPING", "SICK", "SAD", "EXCITED")) {
            val other = renderCodeyBitmap(1, mood)
            assertFalse("Egg ignores $mood", happy.sameAs(other))
            other.recycle()
        }
        val animated = renderCodeyBitmap(1, "HAPPY", time = 50f)
        assertFalse(happy.sameAs(animated))
        happy.recycle(); animated.recycle()
    }

    @Test
    fun renderVisualReviewGallery() {
        val gallery = Bitmap.createBitmap(1080, 1100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(gallery)
        canvas.drawColor(android.graphics.Color.rgb(15, 22, 36))
        val label = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.WHITE; textSize = 16f }
        themes.entries.forEachIndexed { i, (scene, name) ->
            val bitmap = frame(scene, 0.125f)
            canvas.drawBitmap(bitmap, i * 360f, 30f, null)
            canvas.drawText(name, i * 360f + 12f, 22f, label)
            bitmap.recycle()
        }
        listOf(1, 5, 10, 25, 50).forEachIndexed { i, level ->
            val bitmap = renderCodeyBitmap(level, "HAPPY", width = 216, height = 300, time = 17f)
            canvas.drawBitmap(bitmap, i * 216f, 760f, null)
            bitmap.recycle()
        }
        val file = File("build/reports/visual/theme-and-codey-gallery.png")
        requireNotNull(file.parentFile).mkdirs()
        file.outputStream().use { gallery.compress(Bitmap.CompressFormat.PNG, 100, it) }
        gallery.recycle()
    }
}
