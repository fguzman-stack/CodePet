package com.tamagotchi.code.ui.components

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/** One snapshot of the same procedural renderer used by [CodeySprite], never persisted. */
internal fun renderCodeyBitmap(
    level: Int,
    status: String,
    isDead: Boolean = false,
    width: Int = 360,
    height: Int = 380,
    time: Float = 0f,
    pixelMode: Boolean = false
): Bitmap {
    require(width > 0 && height > 0)
    if (pixelMode) return renderCodeyPixelBitmap(level, status, isDead, width, height, time)
    val bitmap = createBitmap(width, height)
    CanvasDrawScope().draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = Canvas(bitmap.asImageBitmap()),
        size = Size(width.toFloat(), height.toFloat())
    ) {
        drawCodeyFrame(PetEvolutionStage.fromLevel(level), codeyMood(status, isDead), time)
    }
    return bitmap
}
