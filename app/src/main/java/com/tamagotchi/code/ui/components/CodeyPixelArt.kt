package com.tamagotchi.code.ui.components

// Retro Pixel mascot mode: renders the SVG-derived programmer cat into a tiny
// off-screen bitmap, quantizes it to a fixed NES-like palette and upscales it
// with nearest-neighbour filtering so every stage, mood and the death scene
// automatically gain a coherent 8-bit look (one single source of truth).

import android.graphics.Bitmap
import com.tamagotchi.code.data.PetOutfit
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/** Palette used to quantize Codey's sprite; mirrors NES-ish saturated colors. */
internal object RetroPixelPalette {
    val colors: IntArray = intArrayOf(
        // The author's fur, patches, glasses, props and legendary cape.
        0xFFFFFEFA.toInt(), 0xFFFFF6EA.toInt(), 0xFFF4E3CB.toInt(),
        0xFFD99A58.toInt(), 0xFFC98A4B.toInt(), 0xFFB97A3C.toInt(),
        0xFFE3B27A.toInt(), 0xFFD9A56B.toInt(), 0xFFC99557.toInt(),
        0xFF5B3A29.toInt(), 0xFF2A1B14.toInt(), 0xFF8A5A33.toInt(),
        0xFFF6A5B5.toInt(), 0xFFFFB3C0.toInt(), 0xFFF08CA0.toInt(),
        0xFF27408B.toInt(), 0xFFE6F1FF.toInt(), 0xFF1E2A32.toInt(),
        0xFF3A4A56.toInt(), 0xFF7EE0A8.toInt(), 0xFF6FB1FF.toInt(),
        0xFF8CB8A8.toInt(), 0xFFB9B4A8.toInt(), 0xFFE5534B.toInt(),
        0xFFD64545.toInt(), 0xFFB83232.toInt(), 0xFFFFD43B.toInt(),
        0xFF7A4FCB.toInt(), 0xFF4A2E9A.toInt(), 0xFF2B1B6E.toInt(),
        0xFF3B3B8F.toInt(), 0xFF1E1E3A.toInt(), 0xFF2B2B52.toInt(),
        0xFFF2C14E.toInt(), 0xFFE8B93A.toInt(), 0xFFFFF3B8.toInt(),
        // Additional wardrobe and status colors.
        0xFFE87990.toInt(), 0xFFAA4566.toInt(), 0xFF27384C.toInt(), 0xFF256C81.toInt(),
        0xFF184654.toInt(), 0xFFE8BD35.toInt(), 0xFF977B22.toInt(), 0xFF8574C2.toInt(),
        0xFF564B7E.toInt(), 0xFF9F75EA.toInt(), 0xFF3675A7.toInt(), 0xFF234C6D.toInt(),
        0xFFFFFFFF.toInt(), 0xFFD0D0D0.toInt(), 0xFF989898.toInt(),
        0xFF606060.toInt(), 0xFF303030.toInt(),
        0xFF000000.toInt()
    )

    private val rArr = IntArray(colors.size) { (colors[it] shr 16) and 0xFF }
    private val gArr = IntArray(colors.size) { (colors[it] shr 8) and 0xFF }
    private val bArr = IntArray(colors.size) { colors[it] and 0xFF }

    /** Nearest palette entry for an RGB triple (channel-weighted distance). */
    fun nearest(r: Int, g: Int, b: Int): Int {
        var best = 0
        var bestDist = Int.MAX_VALUE
        for (i in colors.indices) {
            val dr = r - rArr[i]; val dg = g - gArr[i]; val db = b - bArr[i]
            val dist = 2 * dr * dr + 4 * dg * dg + 3 * db * db
            if (dist < bestDist) { bestDist = dist; best = i }
        }
        return colors[best]
    }
}

/** Palette-locked opaque pixels and transparent silhouette edges, without soft AA. */
private fun quantizePixels(pixels: IntArray) {
    for (i in pixels.indices) {
        val px = pixels[i]
        val a = (px ushr 24) and 0xFF
        if (a < 96) { pixels[i] = 0; continue }
        val q = RetroPixelPalette.nearest((px shr 16) and 0xFF, (px shr 8) and 0xFF, px and 0xFF)
        pixels[i] = q
    }
}

// A fixed design grid preserves eyes and garments at every display density.
private const val RETRO_GRID_W = 96
private const val RETRO_GRID_H = 112

private data class PixelFrameKey(val stage: PetEvolutionStage, val mood: PetMood, val tick: Int,
    val outfit: PetOutfit, val evolution: Int)

// Six-tick poses are shared across redraws; the bounded cache owns its bitmaps.
// Removed entries are left to GC, since another drawing thread may still use one.
private val pixelFrames = android.util.LruCache<PixelFrameKey, Bitmap>(24)

/**
 * Low-resolution palette-quantized sample. The cache owns the bitmap; do not recycle it.
 */
private fun codeyPixelSample(
    stage: PetEvolutionStage,
    mood: PetMood,
    time: Float,
    width: Int,
    height: Int,
    outfit: PetOutfit,
    evolution: Float = 0f
): Bitmap {
    val key = PixelFrameKey(stage, mood, if (mood == PetMood.DEAD) 0 else (time / 6f).toInt() * 6,
        outfit, if (mood == PetMood.DEAD) 0 else (evolution * 20).toInt())
    synchronized(pixelFrames) {
        pixelFrames.get(key)?.let { return it }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        CanvasDrawScope().draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = ComposeCanvas(bitmap.asImageBitmap()),
            size = Size(width.toFloat(), height.toFloat())
        ) {
            // Discrete poses prevent subpixel shimmer while keeping the same animation.
            drawCodeyFrame(stage, mood, key.tick.toFloat(), key.evolution / 20f, outfit)
        }
        val w = bitmap.width
        val h = bitmap.height
        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
        quantizePixels(pixels)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        pixelFrames.put(key, bitmap)
        return bitmap
    }
}

private val nearestPaint = Paint().apply {
    isFilterBitmap = false
    isDither = false
    isAntiAlias = false
}

/**
 * Full-size pixelated Codey for non-Compose consumers (launcher widget).
 * Same defaults as [renderCodeyBitmap]; [pixelMode] enables the 8-bit pass.
 */
internal fun renderCodeyPixelBitmap(
    level: Int,
    status: String,
    isDead: Boolean,
    width: Int,
    height: Int,
    time: Float = 0f,
    outfit: PetOutfit = PetOutfit()
): Bitmap {
    val stage = PetEvolutionStage.fromLevel(level)
    val mood = codeyMood(status, isDead)
    val sample = codeyPixelSample(stage, mood, time, RETRO_GRID_W, RETRO_GRID_H, outfit)
    val out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val factor = minOf(width / RETRO_GRID_W.toFloat(), height / RETRO_GRID_H.toFloat())
    val left = (width - RETRO_GRID_W * factor) / 2f
    val top = (height - RETRO_GRID_H * factor) / 2f
    Canvas(out).drawBitmap(sample, null, RectF(left, top, left + RETRO_GRID_W * factor, top + RETRO_GRID_H * factor), nearestPaint)
    return out
}

/**
 * DrawScope entry used by CodeySprite when the active theme is "Retro Pixel":
 * palette-locked sprite upscaled without filtering or distortion.
 */
internal fun DrawScope.drawCodeyPixelFrame(stage: PetEvolutionStage, mood: PetMood, time: Float, outfit: PetOutfit = PetOutfit(), evolution: Float = 0f) {
    val sample = codeyPixelSample(stage, mood, time, RETRO_GRID_W, RETRO_GRID_H, outfit, evolution)
    val factor = minOf(size.width / RETRO_GRID_W, size.height / RETRO_GRID_H)
    val left = (size.width - RETRO_GRID_W * factor) / 2f
    val top = (size.height - RETRO_GRID_H * factor) / 2f
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawBitmap(
            sample, null,
            RectF(left, top, left + RETRO_GRID_W * factor, top + RETRO_GRID_H * factor),
            nearestPaint
        )
    }
}
