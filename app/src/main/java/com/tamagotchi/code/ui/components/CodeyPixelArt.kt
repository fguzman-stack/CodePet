package com.tamagotchi.code.ui.components

// Retro Pixel mascot mode: renders the normal procedural Codey into a tiny
// off-screen bitmap, quantizes it to a fixed NES-like palette and upscales it
// with nearest-neighbour filtering so every stage, mood and the death scene
// automatically gain a coherent 8-bit look (one single source of truth).

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/** Palette used to quantize Codey's sprite; mirrors NES-ish saturated colors. */
internal object RetroPixelPalette {
    val colors: IntArray = intArrayOf(
        0xFFFFFFFF.toInt(), 0xFFB8C4D0.toInt(), 0xFF8090A2.toInt(), 0xFF4D5D6E.toInt(),
        0xFF26323E.toInt(), 0xFF0D151E.toInt(),
        0xFFFFF4C8.toInt(), 0xFFFFD76B.toInt(), 0xFFF0B84C.toInt(), 0xFFE08A1E.toInt(),
        0xFFFF8C00.toInt(), 0xFFFF4500.toInt(), 0xFFB3261E.toInt(), 0xFF7C8B96.toInt(),
        0xFF3C454D.toInt(), 0xFF101418.toInt(), 0xFFB9C3CC.toInt(), 0xFF97A4B0.toInt(),
        0xFF75828F.toInt(), 0xFF505C68.toInt(), 0xFF8FA0AE.toInt(), 0xFF2A323A.toInt(),
        0xFFEDF3F8.toInt(), 0xFFA9C7E8.toInt(), 0xFF4D8FD1.toInt(), 0xFF4D7EA8.toInt(),
        0xFF5AA9E6.toInt(), 0xFF4FC3F7.toInt(), 0xFF00E5FF.toInt(), 0xFF4DE0C4.toInt(),
        0xFF2DD4BF.toInt(), 0xFF52E07A.toInt(), 0xFF2ECC40.toInt(), 0xFF4CAF50.toInt(),
        0xFF1B5E20.toInt(), 0xFF76FF03.toInt(), 0xFFFFC94D.toInt(), 0xFFFFE9A8.toInt(),
        0xFFE0A83C.toInt(), 0xFFC0A080.toInt(), 0xFFDCA6B4.toInt(), 0xFFF4B8C8.toInt(),
        0xFFE88EA2.toInt(), 0xFFFF0055.toInt(), 0xFFFF1744.toInt(), 0xFFFF6B6B.toInt(),
        0xFFEF5D5D.toInt(), 0xFFB57AFF.toInt(), 0xFF8A2BE2.toInt(), 0xFFD946EF.toInt(),
        0xFF00FF41.toInt(), 0xFF122416.toInt(), 0xFFFFDC00.toInt(), 0xFFF4C94D.toInt(),
        0xFFF8F4FF.toInt(), 0xFF03045E.toInt(), 0xFF90CAF9.toInt(), 0xFFB2EBF2.toInt(),
        0xFF5D4037.toInt(), 0xFF3A2A20.toInt(), 0xFF6E5C41.toInt(), 0xFF9F8A66.toInt(),
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

/** Quantize an ARGB pixel buffer in place; soft AA edges keep their alpha. */
private fun quantizePixels(pixels: IntArray) {
    for (i in pixels.indices) {
        val px = pixels[i]
        val a = (px ushr 24) and 0xFF
        if (a < 10) { pixels[i] = 0; continue }
        val q = RetroPixelPalette.nearest((px shr 16) and 0xFF, (px shr 8) and 0xFF, px and 0xFF)
        pixels[i] = (q and 0x00FFFFFF) or (a shl 24)
    }
}

private const val RETRO_BLOCK_PX = 8

/**
 * Low-resolution palette-quantized sample of Codey. Caller owns the bitmap.
 */
private fun codeyPixelSample(
    stage: PetEvolutionStage,
    mood: PetMood,
    time: Float,
    width: Int,
    height: Int
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    CanvasDrawScope().draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = ComposeCanvas(bitmap.asImageBitmap()),
        size = Size(width.toFloat(), height.toFloat())
    ) {
        drawCodeyFrame(stage, mood, time)
    }
    val w = bitmap.width
    val h = bitmap.height
    val pixels = IntArray(w * h)
    bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
    quantizePixels(pixels)
    bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
    return bitmap
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
    time: Float = 0f
): Bitmap {
    val stage = PetEvolutionStage.fromLevel(level)
    val mood = codeyMood(status, isDead)
    val sampleW = (width / RETRO_BLOCK_PX).coerceAtLeast(24)
    val sampleH = (height / RETRO_BLOCK_PX).coerceAtLeast(24)
    val sample = codeyPixelSample(stage, mood, time, sampleW, sampleH)
    val out = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Canvas(out).drawBitmap(sample, null, RectF(0f, 0f, width.toFloat(), height.toFloat()), nearestPaint)
    sample.recycle()
    return out
}

/**
 * DrawScope entry used by CodeySprite when the active theme is "Retro Pixel":
 * pixelated sprite upscaled to the canvas plus a subtle CRT scanline pass.
 */
internal fun DrawScope.drawCodeyPixelFrame(stage: PetEvolutionStage, mood: PetMood, time: Float) {
    val sampleW = (size.width / RETRO_BLOCK_PX).roundToInt().coerceIn(24, 96)
    val sampleH = (size.height / RETRO_BLOCK_PX).roundToInt().coerceIn(24, 96)
    val sample = codeyPixelSample(stage, mood, time, sampleW, sampleH)
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawBitmap(
            sample, null,
            RectF(0f, 0f, size.width, size.height),
            nearestPaint
        )
    }
    sample.recycle()
    var y = 0f
    while (y < size.height) {
        drawRect(Color.Black.copy(alpha = 0.05f), topLeft = Offset(0f, y), size = Size(size.width, 1f))
        y += 3f
    }
}
