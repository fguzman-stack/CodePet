package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

internal enum class ScenicTheme { OCEAN, JAPANESE, AURORA }

// Stable normalized seeds: no particles or random generators allocated per frame.
private val scenerySeeds = Random(812).let { random ->
    List(48) { floatArrayOf(random.nextFloat(), random.nextFloat(), random.nextFloat()) }
}
private const val TAU = (2.0 * PI).toFloat()

@Composable
internal fun ScenicThemeBackground(scene: ScenicTheme, reduceMotion: Boolean) {
    if (reduceMotion) {
        Canvas(Modifier.fillMaxSize()) { drawScenicFrame(scene, 0f) }
    } else {
        val transition = rememberInfiniteTransition(label = "scenery_$scene")
        val phase by transition.animateFloat(
            initialValue = 0f, targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(48000, easing = LinearEasing)),
            label = "scenery_phase"
        )
        Canvas(Modifier.fillMaxSize()) { drawScenicFrame(scene, phase) }
    }
}

/** Renderer tests inspect animation frames at exact times using this same painter. */
internal fun DrawScope.drawScenicFrame(scene: ScenicTheme, phase: Float) {
    when (scene) {
        ScenicTheme.OCEAN -> drawUnderwaterScene(phase)
        ScenicTheme.JAPANESE -> drawJapaneseScene(phase)
        ScenicTheme.AURORA -> drawPolarScene(phase)
    }
}

private fun DrawScope.drawUnderwaterScene(phase: Float) {
    val w = size.width
    val h = size.height
    val t = phase * TAU
    drawRect(Brush.verticalGradient(listOf(Color(0xFF06354B), Color(0xFF062137), Color(0xFF020D1E))))
    repeat(5) { i ->
        val x = w * (i / 4f) + sin(t + i) * w * 0.05f
        val ray = Path().apply {
            moveTo(x - w * 0.025f, 0f); lineTo(x + w * 0.025f, 0f)
            lineTo(x + w * 0.22f, h * 0.8f); lineTo(x - w * 0.09f, h * 0.8f); close()
        }
        drawPath(ray, Brush.verticalGradient(listOf(Color(0xFF8DE9ED).copy(alpha = 0.10f), Color.Transparent),
            startY = 0f, endY = h * 0.8f))
    }
    repeat(4) { layer ->
        val crest = Path()
        repeat(81) { step ->
            val x = w * step / 80f
            val y = h * (0.055f + layer * 0.022f) +
                sin(step / 80f * TAU * 2f + t + layer) * h * 0.012f
            if (step == 0) crest.moveTo(x, y) else crest.lineTo(x, y)
        }
        drawPath(crest, Color(0xFF8EEAF1).copy(alpha = 0.24f - layer * 0.04f),
            style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round))
    }
    repeat(5) { layer ->
        val ripple = Path()
        repeat(81) { step ->
            val x = w * step / 80f
            val y = h * (0.74f + layer * 0.05f) +
                sin(step / 80f * TAU + t + layer * 0.65f) * h * 0.016f
            if (step == 0) ripple.moveTo(x, y) else ripple.lineTo(x, y)
        }
        drawPath(ripple, Color(0xFF48CADE).copy(alpha = 0.09f), style = Stroke(2.dp.toPx()))
    }
    scenerySeeds.take(28).forEachIndexed { i, seed ->
        val travel = (seed[1] + phase * (2 + i % 3)) % 1f
        val radius = (3f + seed[2] * 8f).dp.toPx()
        val x = seed[0] * w + sin(t * 2f + i) * 9.dp.toPx()
        val y = h + radius * 2f - travel * (h + radius * 4f)
        val center = Offset(x, y)
        drawCircle(Brush.radialGradient(listOf(Color.Transparent, Color(0xFF93ECF5).copy(alpha = 0.10f)),
            center, radius), radius, center)
        drawCircle(Color(0xFFB5F2FA).copy(alpha = 0.38f), radius, center, style = Stroke(0.8.dp.toPx()))
        drawArc(Color.White.copy(alpha = 0.55f), 205f, 70f, false,
            Offset(x - radius * 0.65f, y - radius * 0.65f), Size(radius * 1.3f, radius * 1.3f),
            style = Stroke(1.dp.toPx(), cap = StrokeCap.Round))
    }
    repeat(4) { i ->
        val x = ((phase + i * 0.27f) % 1f) * (w + 80.dp.toPx()) - 40.dp.toPx()
        val y = h * (0.36f + i * 0.13f) + sin(t + i) * 5.dp.toPx()
        val r = 5.dp.toPx()
        val fish = Color(0xFF7DBCD3).copy(alpha = 0.13f)
        drawOval(fish, Offset(x, y), Size(r * 2.4f, r))
        drawPath(Path().apply {
            moveTo(x, y + r / 2f); lineTo(x - r, y - r / 3f)
            lineTo(x - r, y + r * 1.3f); close()
        }, fish)
    }
    repeat(8) { i ->
        val x = if (i < 4) w * i * 0.035f else w * (1f - (i - 4) * 0.035f)
        val weed = Path().apply {
            moveTo(x, h)
            cubicTo(x - w * 0.035f, h * 0.94f, x + sin(t + i) * w * 0.06f,
                h * 0.86f, x + sin(t + i) * w * 0.025f, h * (0.81f - i % 3 * 0.025f))
        }
        drawPath(weed, Color(0xFF268B92).copy(alpha = 0.32f), style = Stroke(4.dp.toPx(), cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawSceneryStars(phase: Float, height: Float) {
    scenerySeeds.forEachIndexed { i, seed ->
        drawCircle(Color(0xFFEAF2FF).copy(alpha = 0.18f + (sin(phase * TAU + i) + 1f) * 0.16f),
            (0.5f + seed[2] * 0.6f).dp.toPx(), Offset(seed[0] * size.width, seed[1] * size.height * height))
    }
}

private fun DrawScope.drawJapaneseScene(phase: Float) {
    val w = size.width
    val h = size.height
    val t = phase * TAU
    drawRect(Brush.verticalGradient(listOf(Color(0xFF172B4A), Color(0xFF0C1831), Color(0xFF080F25))))
    drawSceneryStars(phase, 0.6f)
    val moon = Offset(w * 0.78f, h * 0.15f)
    val moonR = w * 0.065f
    drawCircle(Brush.radialGradient(listOf(Color(0xFFEBDCC0).copy(alpha = 0.13f), Color.Transparent),
        moon, moonR * 3f), moonR * 3f, moon)
    drawCircle(Color(0xFFE8DDC8).copy(alpha = 0.4f), moonR, moon)
    drawCircle(Color(0xFFB5B8BE).copy(alpha = 0.18f), moonR * 0.2f, moon + Offset(moonR * 0.3f, moonR * 0.2f))
    repeat(3) { layer ->
        val base = h * (0.64f + layer * 0.09f)
        val ridge = Path().apply {
            moveTo(0f, h); lineTo(0f, base)
            cubicTo(w * 0.15f, base, w * 0.23f, base - h * 0.07f, w * 0.34f, base - h * 0.13f)
            lineTo(w * 0.45f, base - h * 0.03f)
            cubicTo(w * 0.60f, base - h * 0.08f, w * 0.75f, base - h * 0.18f, w, base - h * 0.02f)
            lineTo(w, h); close()
        }
        drawPath(ridge, listOf(Color(0xFF243750), Color(0xFF172A42), Color(0xFF0B1930))[layer])
    }
    val gateX = w * 0.74f
    val gateY = h * 0.78f
    val gateW = w * 0.32f
    val gateH = h * 0.15f
    val red = Color(0xFFA74642)
    listOf(-1f, 1f).forEach { side ->
        drawLine(red, Offset(gateX + side * gateW * 0.3f, gateY),
            Offset(gateX + side * gateW * 0.34f, gateY + gateH), 5.dp.toPx())
        drawLine(Color(0xFFD77560).copy(alpha = 0.45f), Offset(gateX + side * gateW * 0.3f - 1.dp.toPx(), gateY),
            Offset(gateX + side * gateW * 0.34f - 1.dp.toPx(), gateY + gateH), 1.dp.toPx())
    }
    drawLine(red, Offset(gateX - gateW * 0.42f, gateY + gateH * 0.2f),
        Offset(gateX + gateW * 0.42f, gateY + gateH * 0.2f), 4.dp.toPx())
    val lintel = Path().apply {
        moveTo(gateX - gateW / 2f, gateY - gateH * 0.06f)
        quadraticTo(gateX, gateY + gateH * 0.08f, gateX + gateW / 2f, gateY - gateH * 0.06f)
    }
    drawPath(lintel, red, style = Stroke(6.dp.toPx(), cap = StrokeCap.Round))
    drawLine(red, Offset(gateX, gateY), Offset(gateX, gateY + gateH * 0.2f), 3.dp.toPx())
    repeat(3) { i ->
        val x = w * (if (i == 1) 0.96f else 0.04f + i * 0.035f)
        val y = h * (0.30f + i * 0.06f) + sin(t + i) * 2.dp.toPx()
        val r = 8.dp.toPx()
        val center = Offset(x, y)
        drawLine(Color(0xFFB8A88F).copy(alpha = 0.3f), Offset(x, 0f), Offset(x, y - r), 0.6.dp.toPx())
        drawCircle(Brush.radialGradient(listOf(Color(0xFFFFB75D).copy(alpha = 0.18f), Color.Transparent),
            center, r * 4f), r * 4f, center)
        drawRoundRect(Brush.horizontalGradient(listOf(Color(0xFF985033), Color(0xFFFFCC88), Color(0xFF985033)).map { it.copy(alpha = 0.5f) },
            startX = x - r * 0.65f, endX = x + r * 0.65f),
            Offset(x - r * 0.65f, y - r), Size(r * 1.3f, r * 2f), CornerRadius(r * 0.4f))
        for (rib in -1..1) drawLine(Color(0xFF6F362B).copy(alpha = 0.45f),
            Offset(x - r * 0.6f, y + rib * r * 0.5f), Offset(x + r * 0.6f, y + rib * r * 0.5f), 0.6.dp.toPx())
    }
    repeat(3) { i ->
        val y = h * (0.65f + i * 0.09f)
        drawOval(Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFF9EACC4).copy(alpha = 0.06f), Color.Transparent)),
            Offset(sin(t + i) * w * 0.15f - w * 0.2f, y), Size(w * 1.4f, h * 0.055f))
    }
    scenerySeeds.take(12).forEachIndexed { i, seed ->
        val x = ((seed[0] + phase) % 1f) * (w + 20.dp.toPx()) - 10.dp.toPx()
        val y = ((seed[1] + phase * 2f) % 1f) * (h + 20.dp.toPx()) - 10.dp.toPx()
        val r = (2f + seed[2] * 2f).dp.toPx()
        rotate(sin(t + i) * 50f, Offset(x, y)) {
            drawOval(Color(0xFFF2BAB8).copy(alpha = 0.34f), Offset(x, y), Size(r, r * 1.7f))
        }
    }
}

private fun DrawScope.drawPolarScene(phase: Float) {
    val w = size.width
    val h = size.height
    val t = phase * TAU
    drawRect(Brush.verticalGradient(listOf(Color(0xFF06152A), Color(0xFF071124), Color(0xFF030814))))
    drawSceneryStars(phase, 0.72f)
    val colors = listOf(Color(0xFF60EFB5), Color(0xFF56CFE5), Color(0xFFA88BEB))
    // Connected, bounded ribbons with a soft upper edge instead of full-screen strips.
    colors.forEachIndexed { band, color ->
        fun crest(fraction: Float): Float = h * (0.22f + band * 0.06f) +
            sin(fraction * TAU + t + band * 1.4f) * h * 0.035f +
            sin(fraction * TAU * 2f - t + band) * h * 0.014f
        val ribbon = Path().apply {
            moveTo(0f, crest(0f))
            for (step in 1..100) lineTo(w * step / 100f, crest(step / 100f))
            for (step in 100 downTo 0) {
                val f = step / 100f
                lineTo(w * f, crest(f) - h * (0.09f + 0.025f * sin(f * TAU + t + band)))
            }
            close()
        }
        drawPath(ribbon, Brush.verticalGradient(
            0f to Color.Transparent, 0.25f to color.copy(alpha = 0.035f),
            0.75f to color.copy(alpha = 0.16f), 1f to Color.Transparent,
            startY = h * (0.06f + band * 0.06f), endY = h * (0.28f + band * 0.06f)))
        // Fine luminous folds stay inside the connected curtain, never across the screen.
        clipPath(ribbon) {
            repeat(90) { strip ->
                val fraction = strip / 89f
                val x = w * fraction
                val bottom = crest(fraction)
                val top = bottom - h * 0.12f
                val shimmer = 0.025f + (sin(fraction * TAU * 5f + t * 2f + band) + 1f) * 0.025f
                drawLine(Brush.verticalGradient(listOf(Color.Transparent, color.copy(alpha = shimmer), Color.Transparent),
                    startY = top, endY = bottom + h * 0.01f), Offset(x, top), Offset(x, bottom), w / 90f)
            }
        }
        val glow = Path().apply {
            moveTo(0f, crest(0f))
            for (step in 1..100) lineTo(w * step / 100f, crest(step / 100f))
        }
        drawPath(glow, color.copy(alpha = 0.025f), style = Stroke(12.dp.toPx(), cap = StrokeCap.Round))
        drawPath(glow, color.copy(alpha = 0.10f), style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
    }
    drawRect(Brush.verticalGradient(listOf(Color.Transparent, colors[0].copy(alpha = 0.045f)),
        startY = h * 0.65f, endY = h),
        Offset(0f, h * 0.65f), Size(w, h * 0.35f))
    val horizon = Path().apply {
        moveTo(0f, h); lineTo(0f, h * 0.88f)
        lineTo(w * 0.15f, h * 0.85f); lineTo(w * 0.28f, h * 0.89f)
        lineTo(w * 0.45f, h * 0.82f); lineTo(w * 0.56f, h * 0.87f)
        lineTo(w * 0.76f, h * 0.84f); lineTo(w, h * 0.9f); lineTo(w, h); close()
    }
    drawPath(horizon, Color(0xFF050E1C))
}
