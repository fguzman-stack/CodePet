package com.tamagotchi.code.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.tamagotchi.code.TestApplication
import com.tamagotchi.code.R
import com.tamagotchi.code.data.PetOutfit
import androidx.test.core.app.ApplicationProvider
import java.io.File
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, sdk = [35])
class ProgrammerCatArtworkTest {
    private val levels = listOf(1, 5, 10, 25, 50)

    @Test fun restingPosePreservesEveryOriginalSvgLayer() {
        levels.forEachIndexed { phase, level ->
            val original = Bitmap.createBitmap(240, 280, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(original)
            CatSvgRenderer(ProgrammerCatArt.phases[phase]).layers.forEach { canvas.drawPicture(it.picture) }
            val rendered = renderCodeyBitmap(level, "HAPPY", width = 240, height = 280)
            assertTrue("Phase ${phase + 1} changed the author's resting pose", original.sameAs(rendered))
            assertFalse(ProgrammerCatArt.phases[phase].contains("NIVEL"))
            assertFalse(ProgrammerCatArt.phases[phase].contains("Fase "))
            original.recycle(); rendered.recycle()
        }
    }

    @Test fun allFivePhasesUseTheConfirmedExperienceThresholds() {
        for (level in 1..60) {
            val expected = when { level < 5 -> 0; level < 10 -> 1; level < 25 -> 2; level < 50 -> 3; else -> 4 }
            assertEquals(expected, PetEvolutionStage.fromLevel(level).artIndex)
        }
    }

    @Test fun blinkingActuallyChangesEyesInEveryPhaseAndBothModes() {
        for (pixel in listOf(false, true)) for (level in levels) {
            // Compare just the face's eye area, away from the moving props.
            val awake = renderCodeyBitmap(level, "HAPPY", width = 240, height = 280, time = 0f, pixelMode = pixel)
            val blink = renderCodeyBitmap(level, "HAPPY", width = 240, height = 280, time = 234f, pixelMode = pixel)
            val a = catAnchors[PetEvolutionStage.fromLevel(level).artIndex]
            val eyeTop = (240 + a.eyeY - 13).toInt()
            var changed = false
            for (x in 80..160) for (y in eyeTop..eyeTop + 26) {
                if (awake.getPixel(x, y) != blink.getPixel(x, y)) changed = true
            }
            assertTrue("Eyes never blink: $level/$pixel", changed)
            awake.recycle(); blink.recycle()
        }
    }

    @Test fun everyMoodAndOutfitFitsEvenDuringAnimationInBothModes() {
        for (pixel in listOf(false, true)) for (level in levels) for (mood in PetMood.entries) {
            for (time in listOf(0f, 45f, 132f, 234f)) {
                val outfit = PetOutfit("top_hat", "dev_jacket")
                val image = renderCodeyBitmap(level, mood.name, width = 120, height = 80, time = time, pixelMode = pixel, outfit = outfit)
                assertTrue("Clipped cat $level/$mood/$pixel/$time", (0 until 120).all {
                    Color.alpha(image.getPixel(it, 0)) == 0 && Color.alpha(image.getPixel(it, 79)) == 0
                } && (0 until 80).all {
                    Color.alpha(image.getPixel(0, it)) == 0 && Color.alpha(image.getPixel(119, it)) == 0
                })
                image.recycle()
            }
        }
    }

    @Test fun deathIsFrozenInRetroAsWellAsNormalMode() {
        for (pixel in listOf(false, true)) for (level in levels) {
            val dead = renderCodeyBitmap(level, "DEAD", pixelMode = pixel)
            val overridden = renderCodeyBitmap(level, "EXCITED", isDead = true, time = 234f, pixelMode = pixel)
            assertTrue(dead.sameAs(overridden))
            dead.recycle(); overridden.recycle()
        }
    }

    @Test fun launcherVectorPreservesTheOriginalKittenHead() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val actual = Bitmap.createBitmap(432, 432, Bitmap.Config.ARGB_8888)
        val drawable = requireNotNull(context.getDrawable(R.drawable.ic_launcher_foreground))
        drawable.setBounds(0, 0, 432, 432)
        drawable.draw(Canvas(actual))
        val expected = Bitmap.createBitmap(432, 432, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(expected)
        canvas.scale(4f, 4f)
        canvas.translate(54f, 92f)
        canvas.scale(.38f, .38f)
        canvas.translate(-120f, -240f)
        CatSvgRenderer(ProgrammerCatArt.phases[0]).layers.filter {
            it.part in listOf("head", "eye", "eye_shine", "mouth")
        }.forEach { canvas.drawPicture(it.picture) }
        var error = 0L
        for (x in 0 until 432) for (y in 0 until 432) {
            val a = actual.getPixel(x, y); val b = expected.getPixel(x, y)
            error += kotlin.math.abs(Color.alpha(a) - Color.alpha(b))
            // RGB in fully transparent pixels is undefined.
            if (Color.alpha(a) > 0 && Color.alpha(b) > 0) {
                error += kotlin.math.abs(Color.red(a) - Color.red(b))
                error += kotlin.math.abs(Color.green(a) - Color.green(b))
                error += kotlin.math.abs(Color.blue(a) - Color.blue(b))
            }
        }
        assertTrue("Launcher no longer matches the SVG head", error / (432.0 * 432 * 4 * 255) < .012)
        actual.recycle(); expected.recycle()
    }

    @Test fun exportIconVisualReview() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val image = Bitmap.createBitmap(1080, 216, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(image)
        canvas.drawColor(Color.rgb(27, 32, 43))
        listOf(R.drawable.ic_codey_logo, R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_monochrome, R.drawable.ic_stat_codey, R.mipmap.ic_launcher).forEachIndexed { i, id ->
            requireNotNull(context.getDrawable(id)).apply {
                setBounds(i * 216, 0, (i + 1) * 216, 216)
                draw(canvas)
            }
        }
        val file = File("build/reports/cat/icons.png")
        requireNotNull(file.parentFile).mkdirs()
        file.outputStream().use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
        image.recycle()
    }

    @Test fun exportCatVisualReview() {
        for (pixel in listOf(false, true)) {
            val image = Bitmap.createBitmap(240 * 8, 280 * 5, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawColor(Color.rgb(27, 32, 43))
            val label = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = 13f }
            levels.forEachIndexed { row, level ->
                PetMood.entries.forEachIndexed { column, mood ->
                    val cat = renderCodeyBitmap(level, mood.name, width = 240, height = 280, pixelMode = pixel)
                    canvas.drawBitmap(cat, column * 240f, row * 280f, null)
                    canvas.drawText("$level / $mood", column * 240f + 8, row * 280f + 18, label)
                    cat.recycle()
                }
            }
            val file = File("build/reports/cat/${if (pixel) "retro" else "normal"}.png")
            requireNotNull(file.parentFile).mkdirs()
            file.outputStream().use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
            image.recycle()
        }
    }
}
