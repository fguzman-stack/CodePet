package com.tamagotchi.code.ui.components

// Procedural robot adapted from the supplied HTML/Canvas reference.
// Rendering is shared with RemoteViews through CodeyBitmap.

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// UI-only adapter for the persisted String status; evolution uses the existing project type.
internal enum class PetMood { HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD }

internal fun codeyMood(status: String, isDead: Boolean = false): PetMood =
    if (isDead) PetMood.DEAD else PetMood.entries.firstOrNull { it.name == status } ?: PetMood.HAPPY

/**
 * Per-stage material palette. The robot keeps its metallic identity but each
 * evolution stage owns a distinct tint: Child is bright white-mint, Adult is
 * cool blue steel, Veteran dark gunmetal with gold, Legendary pearl with gold.
 */
internal data class StagePalette(
    val light: Color,
    val body: Color,
    val mid: Color,
    val dark: Color,
    val edge: Color,
    val visor: Color,
    val trim: Color
)

internal val StagePalettes: Map<PetEvolutionStage, StagePalette> = mapOf(
    PetEvolutionStage.Child to StagePalette(
        light = Color(0xFFF9FCFF), body = Color(0xFFE9F0F8), mid = Color(0xFFC4D4E2),
        dark = Color(0xFF8FA5B8), edge = Color(0xFF5C7288), visor = Color(0xFF0B1A29),
        trim = Color(0xFF7BE0C8)
    ),
    PetEvolutionStage.Adult to StagePalette(
        light = Color(0xFFEFF6FE), body = Color(0xFFCBDCEC), mid = Color(0xFF9DB8CF),
        dark = Color(0xFF6B87A1), edge = Color(0xFF41586E), visor = Color(0xFF0A1826),
        trim = Color(0xFF4FC3F7)
    ),
    PetEvolutionStage.Veteran to StagePalette(
        light = Color(0xFFCDD8E2), body = Color(0xFF9CACBC), mid = Color(0xFF75889A),
        dark = Color(0xFF4D5E6E), edge = Color(0xFF2E3B47), visor = Color(0xFF0A121C),
        trim = Color(0xFFFFC85C)
    ),
    PetEvolutionStage.Legendary to StagePalette(
        light = Color(0xFFFFFCF2), body = Color(0xFFF3EAD9), mid = Color(0xFFDAC9A9),
        dark = Color(0xFF9F8A66), edge = Color(0xFF6F5D42), visor = Color(0xFF131226),
        trim = Color(0xFFFFD76B)
    )
)

internal fun stagePalette(stage: PetEvolutionStage): StagePalette =
    StagePalettes[stage] ?: StagePalettes.getValue(PetEvolutionStage.Child)

private fun moodColor(mood: PetMood): Color = when (mood) {
    PetMood.HAPPY -> Color(0xFF4DE0C4)
    PetMood.SLEEPING -> Color(0xFF4D8FD1)
    PetMood.STUDYING -> Color(0xFF52E07A)
    PetMood.SICK -> Color(0xFFEF5D5D)
    PetMood.HUNGRY -> Color(0xFFF0B84C)
    PetMood.SAD -> Color(0xFF4D7EA8)
    PetMood.EXCITED -> Color(0xFFF4C94D)
    PetMood.DEAD -> Color(0xFF3C454D)
}

private data class StageConfig(
    val headR: Float, val torsoW: Float, val torsoH: Float,
    val armW: Float, val armH: Float, val legW: Float, val legH: Float,
    val antennas: Int, val shoulderArmor: Boolean, val wings: Boolean, val halo: Boolean,
    val coreR: Float, val baseY: Float
)

// Chibi proportions: oversized head, compact rounded chassis.
private val STAGE_CONFIG = mapOf(
    PetEvolutionStage.Child to StageConfig(40f, 58f, 48f, 15f, 26f, 18f, 16f, 1, false, false, false, 9f, 205f),
    PetEvolutionStage.Adult to StageConfig(42f, 66f, 54f, 16f, 30f, 20f, 18f, 1, false, false, false, 10f, 200f),
    PetEvolutionStage.Veteran to StageConfig(44f, 72f, 58f, 17f, 32f, 21f, 20f, 2, true, false, false, 11f, 197f),
    PetEvolutionStage.Legendary to StageConfig(46f, 78f, 62f, 18f, 34f, 22f, 22f, 3, true, true, true, 12f, 192f)
)

// Design space is 360x380; the Composable scales this to fit its own size.
private const val DESIGN_W = 360f
private const val DESIGN_H = 380f

@Composable
fun CodeySprite(
    status: String,
    level: Int = 1,
    isDead: Boolean = false,
    celebrationTrigger: SharedFlow<Unit>? = null,
    learningEventTrigger: SharedFlow<String>? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    animationEnabled: Boolean = true
) {
    val mood = codeyMood(status, isDead)
    val stage = PetEvolutionStage.fromLevel(level)
    val reduceMotion = LocalReduceMotion.current
    val pixelMode = LocalAppTheme.current.name == "Retro Pixel"
    val animate = animationEnabled && !reduceMotion && mood != PetMood.DEAD
    var celebrating by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    LaunchedEffect(celebrationTrigger, animate) {
        celebrating = false
        celebrationTrigger?.collect {
            if (animate) {
                celebrating = true
                delay(650)
                celebrating = false
            }
        }
    }
    LaunchedEffect(learningEventTrigger, animate) {
        failed = false
        learningEventTrigger?.collect { event ->
            if (animate) {
                if (event == "SUCCESS") celebrating = true else if (event == "FAILURE") failed = true
                delay(400)
                celebrating = false
                failed = false
            }
        }
    }
    // 2513 ticks (~41.9 s at 60 Hz) closes every sin phase used below, so the
    // Restart wrap is seamless and blink period (139 ticks) is preserved.
    val clock = rememberInfiniteTransition(label = "CodeyClock")
    val ticks by clock.animateFloat(
        initialValue = 0f,
        targetValue = 2513f,
        animationSpec = infiniteRepeatable(
            animation = tween(41883, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ticks"
    )
    val description = stringResource(R.string.cd_widget_pet)
    Canvas(
        modifier = modifier
            .size(170.dp)
            .semantics { contentDescription = description }
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        val t = if (animate) ticks else 0f
        val effectiveMood = if (celebrating && animate) PetMood.EXCITED else mood
        rotate(if (failed && animate) sin(t * 0.8f) * 5f else 0f) {
            if (pixelMode) drawCodeyPixelFrame(stage, effectiveMood, t)
            else drawCodeyFrame(stage, effectiveMood, t)
        }
    }
}

/** Shared by the live Canvas and the launcher's offscreen bitmap. Time is in 60 Hz ticks. */
internal fun DrawScope.drawCodeyFrame(stage: PetEvolutionStage, mood: PetMood, time: Float) {
    val t = if (mood == PetMood.DEAD) 0f else time
    val scaleF = minOf(size.width / DESIGN_W, size.height / DESIGN_H)
    translate((size.width - DESIGN_W * scaleF) / 2f, (size.height - DESIGN_H * scaleF) / 2f) {
        scale(scaleF, scaleF, pivot = Offset.Zero) {
            drawCodey(stage, mood, t, t % 139f > 130f)
        }
    }
}

private fun DrawScope.drawCodey(stage: PetEvolutionStage, mood: PetMood, t: Float, blinking: Boolean) {
    val accent = moodColor(mood)
    val pal = stagePalette(stage)

    if (stage == PetEvolutionStage.Egg) {
        drawShadow(46f, 180f, 292f)
        if (mood == PetMood.DEAD) {
            withTransform({ translate(150f, 252f); rotate(-6f, Offset(30f, 20f)) }) { drawDeadEgg() }
            drawTombstone(250f, 296f, 0.68f)
        } else {
            rotate(0f, Offset(180f, 210f)) { drawEgg(t, accent, pal, powered = true) }
        }
        return
    }

    val cfg = STAGE_CONFIG.getValue(stage)

    if (mood == PetMood.DEAD) {
        drawDeadScene(cfg)
        return
    }

    var bounce = 0f
    var legSwing = 0f
    var armSwing = 0f
    var tilt = 0f

    val isLow = mood == PetMood.SAD || mood == PetMood.SICK
    val speed = if (mood == PetMood.EXCITED) 0.16f else if (isLow) 0.03f else 0.055f
    val amp = if (mood == PetMood.EXCITED) 9f else if (isLow) 2f else 4f
    bounce = sin(t * speed) * amp
    legSwing = sin(t * speed * 1.3f) * (if (mood == PetMood.EXCITED) 0.22f else 0.1f)
    armSwing = when {
        mood == PetMood.EXCITED -> 1.9f + sin(t * speed * 1.6f) * 0.3f
        isLow -> 0.05f + sin(t * speed) * 0.03f
        else -> 0.18f + sin(t * speed * 0.8f) * 0.12f
    }
    tilt = if (isLow) 0.12f else 0f

    drawShadow(cfg.torsoW / 1.6f, 180f, 300f + (cfg.baseY - 200f))
    val cy = drawRobot(mood, cfg, pal, accent, bounce, legSwing, armSwing, tilt, t, blinking)
    drawMoodEffect(mood, accent, cy - (cfg.headR + 40f), t)
}

private fun DrawScope.drawShadow(halfWidth: Float, cx: Float, groundY: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.Black.copy(alpha = 0.35f), Color.Transparent),
            center = Offset(cx, groundY),
            radius = halfWidth
        ),
        radius = halfWidth,
        center = Offset(cx, groundY)
    )
}

private fun DrawScope.metalBrush(pal: StagePalette, w: Float, h: Float, cx: Float, cy: Float): Brush =
    Brush.linearGradient(
        colors = listOf(pal.light, pal.body, pal.mid),
        start = Offset(cx - w / 2, cy - h / 2),
        end = Offset(cx + w / 2, cy + h / 2)
    )

private fun DrawScope.drawTextNative(text: String, x: Float, y: Float, sizePx: Float, color: Color, bold: Boolean = true) {
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = sizePx
            this.color = color.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = bold
        }
        canvas.nativeCanvas.drawText(text, x, y, paint)
    }
}

private fun DrawScope.drawEgg(t: Float, accent: Color, pal: StagePalette, powered: Boolean) {
    val cx = 180f
    val cy = 210f
    withTransform({ translate(cx, cy) }) {
        val path = Path().apply {
            moveTo(0f, -62f)
            cubicTo(34f, -62f, 40f, -10f, 34f, 30f)
            cubicTo(24f, 58f, -24f, 58f, -34f, 30f)
            cubicTo(-40f, -10f, -34f, -62f, 0f, -62f)
            close()
        }
        drawPath(path, brush = Brush.linearGradient(
            listOf(pal.light, pal.body, pal.mid), start = Offset(-40f, -62f), end = Offset(40f, 58f)
        ))
        drawPath(path, color = pal.edge, style = Stroke(width = 3f))

        // zigzag hatch seam
        val seam = Path().apply {
            moveTo(-35f, 2f)
            lineTo(-22f, -8f); lineTo(-9f, 4f); lineTo(5f, -8f); lineTo(18f, 4f); lineTo(35f, -2f)
        }
        drawPath(seam, color = pal.dark.copy(alpha = 0.65f), style = Stroke(width = 2.2f))

        drawOval(pal.visor, topLeft = Offset(-15f, -38f), size = Size(30f, 22f))
        val pulse = if (powered) 0.5f + sin(t * 0.06f) * 0.2f else 0f
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(accent.copy(alpha = pulse), Color.Transparent),
                center = Offset(0f, -27f), radius = 16f
            ),
            topLeft = Offset(-15f, -38f), size = Size(30f, 22f)
        )
        // a single dim "eye-light" so the egg feels alive
        drawCircle(accent.copy(alpha = if (powered) 0.8f else 0.2f), radius = 2.6f, center = Offset(0f, -27f))

        // nub antenna on the shell
        drawLine(pal.edge, Offset(0f, -62f), Offset(0f, -72f), strokeWidth = 3f, cap = StrokeCap.Round)
        drawCircle(pal.trim, radius = 4f, center = Offset(0f, -74f))

        listOf(Offset(-20f, 26f), Offset(20f, 26f), Offset(0f, 46f)).forEach {
            drawCircle(pal.dark.copy(alpha = 0.5f), radius = 3f, center = it)
        }
    }
}

private fun DrawScope.drawLimb(pivot: Offset, w: Float, h: Float, angle: Float, accent: Color, pal: StagePalette) {
    withTransform({
        translate(pivot.x, pivot.y)
        rotate(degrees = Math.toDegrees(angle.toDouble()).toFloat(), pivot = Offset.Zero)
    }) {
        val path = Path().apply {
            addRoundRect(RoundRect(Rect(-w / 2, 0f, w / 2, h), CornerRadius(w / 2, w / 2)))
        }
        drawPath(path, brush = metalBrush(pal, w, h, 0f, h / 2))
        drawPath(path, color = pal.edge, style = Stroke(width = 2f))
        // joint ring + accent core
        drawCircle(pal.dark, radius = w / 2 + 2, center = Offset.Zero)
        drawCircle(accent.copy(alpha = 0.55f), radius = w / 2 - 1, center = Offset.Zero)
        // rounded foot/hand pad
        drawOval(
            brush = Brush.linearGradient(listOf(pal.body, pal.mid), start = Offset(-w * 0.4f, h - w * 0.5f), end = Offset(w * 0.5f, h + w * 0.4f)),
            topLeft = Offset(-w * 0.75f, h - w * 0.45f), size = Size(w * 1.5f, w * 0.95f)
        )
        drawOval(pal.edge, topLeft = Offset(-w * 0.75f, h - w * 0.45f), size = Size(w * 1.5f, w * 0.95f), style = Stroke(width = 1.5f))
    }
}

private fun DrawScope.drawAntenna(x: Float, topY: Float, len: Float, accent: Color, sway: Float, powered: Boolean, pal: StagePalette) {
    withTransform({
        translate(x, topY)
        rotate(degrees = Math.toDegrees(sway.toDouble()).toFloat(), pivot = Offset.Zero)
    }) {
        drawLine(pal.edge, Offset.Zero, Offset(0f, -len), strokeWidth = 3f, cap = StrokeCap.Round)
        if (powered) drawCircle(
            brush = Brush.radialGradient(listOf(accent, Color.Transparent), center = Offset(0f, -len), radius = 10f),
            radius = 10f, center = Offset(0f, -len)
        )
        drawCircle(accent, radius = 4f, center = Offset(0f, -len))
        drawCircle(Color.White.copy(alpha = if (powered) 0.7f else 0.1f), radius = 1.4f, center = Offset(-1.2f, -len - 1.2f))
    }
}

private fun DrawScope.drawWing(x: Float, y: Float, flip: Float, accent: Color, pal: StagePalette, t: Float) {
    withTransform({
        translate(x, y)
        scale(flip, 1f, pivot = Offset.Zero)
        rotate(sin(t * 0.1f) * 3f, pivot = Offset.Zero)
    }) {
        val path = Path().apply {
            moveTo(0f, 0f); lineTo(40f, -16f); lineTo(50f, 6f); lineTo(36f, 34f); lineTo(8f, 28f); close()
        }
        drawPath(path, brush = Brush.linearGradient(listOf(pal.light, pal.mid, pal.dark), start = Offset(0f, -16f), end = Offset(50f, 34f)))
        drawPath(path, color = pal.edge, style = Stroke(width = 1.5f))
        drawPath(path, color = accent.copy(alpha = 0.7f), style = Stroke(width = 1f))
        drawLine(accent.copy(alpha = 0.35f), Offset(6f, 6f), Offset(40f, -6f), strokeWidth = 1.2f)
        drawLine(accent.copy(alpha = 0.35f), Offset(10f, 18f), Offset(38f, 22f), strokeWidth = 1.2f)
    }
}

private fun DrawScope.drawCore(cx: Float, cy: Float, r: Float, mood: PetMood, accent: Color, t: Float, pal: StagePalette) {
    if (mood != PetMood.DEAD) {
        val pulse = 0.75f + sin(t * if (mood == PetMood.EXCITED) 0.25f else 0.08f) * 0.25f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(accent.copy(alpha = 0.4f * pulse), Color.Transparent),
                center = Offset(cx, cy), radius = r * 2.6f
            ),
            radius = r * 2.6f, center = Offset(cx, cy)
        )
    }
    drawCircle(if (mood == PetMood.DEAD) Color(0xFF454F58) else accent, radius = r, center = Offset(cx, cy))
    drawCircle(pal.dark, radius = r, center = Offset(cx, cy), style = Stroke(width = 2f))
    drawCircle(pal.edge, radius = r + 4, center = Offset(cx, cy), style = Stroke(width = 2f))
    // inner shine dot keeps the core from looking flat
    drawCircle(Color.White.copy(alpha = if (mood == PetMood.DEAD) 0.06f else 0.45f), radius = r * 0.3f,
        center = Offset(cx - r * 0.3f, cy - r * 0.3f))
}

private fun DrawScope.drawVisorFace(cx: Float, cy: Float, w: Float, h: Float, mood: PetMood, accent: Color, blinking: Boolean, pal: StagePalette) {
    val topLeft = Offset(cx - w / 2, cy - h / 2)
    drawRoundRect(
        brush = Brush.linearGradient(listOf(pal.visor, pal.visor.copy(alpha = 0.86f)), start = topLeft, end = Offset(cx + w / 2, cy + h / 2)),
        topLeft = topLeft, size = Size(w, h), cornerRadius = CornerRadius(h * 0.34f)
    )
    for (line in 1 until (h / 4f).toInt()) {
        val y = topLeft.y + line * 4f
        drawLine(Color.White.copy(alpha = 0.04f), Offset(topLeft.x + 4f, y), Offset(topLeft.x + w - 4f, y), 0.5f)
    }
    // glass gloss sweep
    drawLine(Color.White.copy(alpha = 0.10f), Offset(topLeft.x + w * 0.18f, topLeft.y + h * 0.16f),
        Offset(topLeft.x + w * 0.5f, topLeft.y + h * 0.1f), strokeWidth = 2.5f, cap = StrokeCap.Round)

    val eyeGap = w * 0.24f
    val eyeY = cy - h * 0.06f
    val s = w / 58f // eye scale relative to visor width
    val closed = blinking && mood != PetMood.DEAD && mood != PetMood.SLEEPING && mood != PetMood.EXCITED
    val stroke = Stroke(width = 3f * s, cap = StrokeCap.Round)

    listOf(-1f, 1f).forEach { side ->
        val ex = cx + side * eyeGap
        if (mood != PetMood.DEAD) {
            drawCircle(Brush.radialGradient(listOf(accent.copy(alpha = 0.25f), Color.Transparent), Offset(ex, eyeY), 12f * s), 12f * s, Offset(ex, eyeY))
        }
        when {
            mood == PetMood.SLEEPING || closed -> {
                drawArc(accent, 180f, 180f, useCenter = false,
                    topLeft = Offset(ex - 6f * s, eyeY - 3f * s), size = Size(12f * s, 8f * s), style = stroke)
            }
            mood == PetMood.SICK -> {
                drawLine(accent, Offset(ex - 5f * s, eyeY - 5f * s), Offset(ex + 5f * s, eyeY + 5f * s), strokeWidth = 3f * s)
                drawLine(accent, Offset(ex + 5f * s, eyeY - 5f * s), Offset(ex - 5f * s, eyeY + 5f * s), strokeWidth = 3f * s)
            }
            mood == PetMood.STUDYING -> {
                val p = Path().apply {
                    moveTo(ex + 4f * s, eyeY - 6f * s); lineTo(ex - 3f * s, eyeY); lineTo(ex + 4f * s, eyeY + 6f * s)
                }
                drawPath(p, accent, style = stroke)
            }
            mood == PetMood.EXCITED -> {
                drawStar(ex, eyeY, 7f * s, accent)
                drawCircle(Color.White.copy(alpha = 0.8f), radius = 1.6f * s, center = Offset(ex - 2f * s, eyeY - 2.5f * s))
            }
            mood == PetMood.SAD -> {
                drawCircle(accent.copy(alpha = 0.9f), radius = 5f * s, center = Offset(ex, eyeY))
                // half-lidded droop
                drawLine(pal.visor, Offset(ex - 6f * s, eyeY - 4f * s), Offset(ex + 6f * s, eyeY - 1.5f * s), strokeWidth = 4f * s)
            }
            mood == PetMood.HUNGRY -> {
                drawCircle(accent, radius = 5f * s, center = Offset(ex, eyeY))
                drawCircle(Color.White.copy(alpha = 0.75f), radius = 1.6f * s, center = Offset(ex + 1.6f * s, eyeY - 1.6f * s))
            }
            else -> {
                drawCircle(accent, radius = 6f * s, center = Offset(ex, eyeY))
                drawCircle(Color.White.copy(alpha = 0.85f), radius = 2f * s, center = Offset(ex + 1.8f * s, eyeY - 2f * s))
            }
        }
    }

    if (mood == PetMood.HAPPY || mood == PetMood.EXCITED || mood == PetMood.HUNGRY) {
        listOf(-1f, 1f).forEach { side ->
            drawCircle(accent.copy(alpha = 0.20f), radius = 4.5f * s, center = Offset(cx + side * eyeGap * 1.7f, eyeY + 8f * s))
        }
    }
    when (mood) {
        PetMood.EXCITED -> drawArc(accent, 0f, 180f, false, Offset(cx - 8f * s, eyeY + 6f * s), Size(16f * s, 13f * s), style = stroke)
        PetMood.SAD -> drawArc(accent, 180f, 180f, false, Offset(cx - 6f * s, eyeY + 14f * s), Size(12f * s, 11f * s), style = stroke)
        PetMood.SICK -> drawPath(Path().apply {
            moveTo(cx - 7f * s, eyeY + 15f * s)
            cubicTo(cx - 3f * s, eyeY + 9f * s, cx, eyeY + 21f * s, cx + 3f * s, eyeY + 15f * s)
            quadraticTo(cx + 5f * s, eyeY + 11f * s, cx + 8f * s, eyeY + 15f * s)
        }, accent, style = stroke)
        PetMood.HUNGRY -> drawOval(accent, topLeft = Offset(cx - 4f * s, eyeY + 9f * s), size = Size(9f * s, 11f * s))
        PetMood.SLEEPING -> drawLine(accent, Offset(cx - 4f * s, eyeY + 14f * s), Offset(cx + 4f * s, eyeY + 14f * s), strokeWidth = 3f * s, cap = StrokeCap.Round)
        PetMood.STUDYING -> drawLine(accent, Offset(cx - 5f * s, eyeY + 14f * s), Offset(cx + 5f * s, eyeY + 14f * s), strokeWidth = 3f * s, cap = StrokeCap.Round)
        else -> drawArc(accent, 0f, 180f, false, Offset(cx - 6f * s, eyeY + 6f * s), Size(12f * s, 11f * s), style = stroke)
    }
}

private fun DrawScope.drawStar(x: Float, y: Float, r: Float, color: Color) {
    val path = Path()
    for (i in 0 until 5) {
        val a1 = Math.toRadians((18 + i * 72).toDouble())
        val a2 = Math.toRadians((54 + i * 72).toDouble())
        val px1 = x + cos(a1).toFloat() * r
        val py1 = y - sin(a1).toFloat() * r
        val px2 = x + cos(a2).toFloat() * r * 0.4f
        val py2 = y - sin(a2).toFloat() * r * 0.4f
        if (i == 0) path.moveTo(px1, py1) else path.lineTo(px1, py1)
        path.lineTo(px2, py2)
    }
    path.close()
    path.fillType = PathFillType.NonZero
    drawPath(path, color)
}

private fun DrawScope.drawRobot(
    mood: PetMood, cfg: StageConfig, pal: StagePalette, accent: Color,
    bounce: Float, legSwing: Float, armSwing: Float, tilt: Float, t: Float, blinking: Boolean
): Float {
    val cx = 180f
    val cy = cfg.baseY + bounce

    withTransform({
        translate(cx, cy)
        rotate(degrees = Math.toDegrees(tilt.toDouble()).toFloat(), pivot = Offset.Zero)
    }) {
        val torsoTop = -cfg.torsoH / 2
        val torsoBottom = cfg.torsoH / 2
        val shoulderY = torsoTop + 6
        val hipY = torsoBottom - 2
        val shoulderX = cfg.torsoW / 2 - 4
        val hipX = cfg.torsoW / 2 - 10

        if (cfg.halo) {
            val hy = torsoTop - cfg.headR * 2 - 14
            val haloAlpha = 0.7f + sin(t * 0.05f) * 0.2f
            drawArc(
                pal.trim.copy(alpha = (haloAlpha * 0.35f).coerceIn(0f, 1f)),
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(-30f, hy - 9f), size = Size(60f, 18f), style = Stroke(width = 7f)
            )
            drawArc(
                pal.trim.copy(alpha = haloAlpha.coerceIn(0f, 1f)),
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(-26f, hy - 7f), size = Size(52f, 14f), style = Stroke(width = 3f)
            )
        }
        if (cfg.wings) {
            drawWing(-shoulderX - 4, shoulderY + 6, -1f, accent, pal, t)
            drawWing(shoulderX + 4, shoulderY + 6, 1f, accent, pal, t)
        }

        drawLimb(Offset(-hipX + 10, hipY), cfg.legW, cfg.legH, legSwing, accent, pal)
        drawLimb(Offset(hipX - 10, hipY), cfg.legW, cfg.legH, -legSwing, accent, pal)

        // torso chassis
        val torsoPath = Path().apply {
            addRoundRect(RoundRect(Rect(-cfg.torsoW / 2, torsoTop, cfg.torsoW / 2, torsoBottom), CornerRadius(16f, 18f)))
        }
        drawPath(torsoPath, brush = metalBrush(pal, cfg.torsoW, cfg.torsoH, 0f, 0f))
        drawPath(torsoPath, color = pal.edge, style = Stroke(width = 2.5f))
        // top gloss
        drawRoundRect(
            Color.White.copy(alpha = 0.22f),
            topLeft = Offset(-cfg.torsoW / 2 + 6, torsoTop + 4), size = Size(cfg.torsoW - 12, 6f),
            cornerRadius = CornerRadius(3f)
        )
        // waist seam + rivets
        drawLine(pal.dark.copy(alpha = 0.4f), Offset(0f, torsoTop + 6), Offset(0f, torsoBottom - 6), strokeWidth = 1.5f)
        listOf(
            Offset(-cfg.torsoW / 2 + 8, torsoTop + 10), Offset(cfg.torsoW / 2 - 8, torsoTop + 10),
            Offset(-cfg.torsoW / 2 + 8, torsoBottom - 8), Offset(cfg.torsoW / 2 - 8, torsoBottom - 8)
        ).forEach { drawCircle(pal.dark.copy(alpha = 0.55f), radius = 2.5f, center = it) }

        drawCore(0f, 4f, cfg.coreR, mood, accent, t, pal)
        // status LEDs beside the core
        for (i in 0 until 3) {
            val on = mood == PetMood.EXCITED || i <= 1 || mood == PetMood.HAPPY
            drawCircle(
                if (on) pal.trim.copy(alpha = 0.9f) else pal.dark.copy(alpha = 0.6f),
                radius = 1.8f, center = Offset(cfg.torsoW / 2 - 12f, torsoTop + 12f + i * 6f)
            )
        }

        if (cfg.shoulderArmor) {
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFFFE9A8), Color(0xFFE0A83C)), center = Offset(cfg.torsoW / 2 - 16, torsoTop + 18), radius = 6f),
                radius = 6f, center = Offset(cfg.torsoW / 2 - 14, torsoTop + 20)
            )
        }

        drawLimb(Offset(-shoulderX, shoulderY), cfg.armW, cfg.armH, armSwing, accent, pal)
        drawLimb(Offset(shoulderX, shoulderY), cfg.armW, cfg.armH, -armSwing, accent, pal)

        if (cfg.shoulderArmor) {
            listOf(-1f, 1f).forEach { s ->
                drawOval(
                    brush = metalBrush(pal, 26f, 20f, s * shoulderX, shoulderY - 6),
                    topLeft = Offset(s * shoulderX - 13, shoulderY - 6 - 10), size = Size(26f, 20f)
                )
                drawOval(
                    pal.trim.copy(alpha = 0.65f),
                    topLeft = Offset(s * shoulderX - 13, shoulderY - 6 - 10), size = Size(26f, 20f),
                    style = Stroke(width = 1.5f)
                )
            }
        }

        // head
        val headY = torsoTop - cfg.headR - 4
        drawCircle(
            brush = Brush.radialGradient(
                listOf(pal.light, pal.body, pal.mid),
                center = Offset(-cfg.headR * 0.35f, headY - cfg.headR * 0.4f),
                radius = cfg.headR * 1.9f
            ),
            radius = cfg.headR, center = Offset(0f, headY)
        )
        drawCircle(pal.edge, radius = cfg.headR, center = Offset(0f, headY), style = Stroke(width = 2.5f))
        // ear pods
        listOf(-1f, 1f).forEach { s ->
            drawCircle(pal.mid, radius = cfg.headR * 0.22f, center = Offset(s * cfg.headR * 0.96f, headY + 2f))
            drawCircle(pal.trim.copy(alpha = 0.8f), radius = cfg.headR * 0.1f, center = Offset(s * cfg.headR * 0.96f, headY + 2f))
        }

        val antTopY = headY - cfg.headR + 4
        for (i in 0 until cfg.antennas) {
            val spread = 18f
            val ax = -((cfg.antennas - 1) * spread) / 2 + i * spread
            val extraLen = if (i == cfg.antennas / 2) 6f else 0f
            drawAntenna(ax, antTopY, 20f + extraLen, accent, sin(t * 0.04f + i) * 0.08f, powered = true, pal)
        }

        drawVisorFace(0f, headY + 3f, cfg.headR * 1.5f, cfg.headR * 1.0f, mood, accent, blinking, pal)
    }

    return cy
}

// ─────────────────────────────────────────────────────────
// Death scene: static, tasteful — slumped robot, cracks,
// rising ghost and an aligned tombstone. Never reads `time`.
// ─────────────────────────────────────────────────────────
private fun DrawScope.drawDeadScene(cfg: StageConfig) {
    val groundY = 300f + (cfg.baseY - 200f)
    val gray = StagePalette(
        light = Color(0xFFB9C3CC), body = Color(0xFF97A4B0), mid = Color(0xFF75828F),
        dark = Color(0xFF505C68), edge = Color(0xFF39434C), visor = Color(0xFF101418),
        trim = Color(0xFF6E7B88)
    )

    drawShadow(cfg.torsoW * 0.9f, 168f, groundY + 2f)

    // oil puddle under the head
    drawOval(Color(0xFF1C242C).copy(alpha = 0.55f), topLeft = Offset(66f, groundY - 14f), size = Size(72f, 14f))

    // slumped chassis: torso tilted and resting on the ground
    withTransform({
        translate(176f, groundY - cfg.torsoH / 2 + 6f)
        rotate(10f)
    }) {
        val torsoPath = Path().apply {
            addRoundRect(RoundRect(Rect(-cfg.torsoW / 2, -cfg.torsoH / 2, cfg.torsoW / 2, cfg.torsoH / 2), CornerRadius(16f, 18f)))
        }
        drawPath(torsoPath, brush = metalBrush(gray, cfg.torsoW, cfg.torsoH, 0f, 0f))
        drawPath(torsoPath, color = gray.edge, style = Stroke(width = 2.5f))
        // dead core with crack through it
        drawCircle(Color(0xFF454F58), radius = cfg.coreR, center = Offset(0f, 2f))
        drawCircle(gray.dark, radius = cfg.coreR, center = Offset(0f, 2f), style = Stroke(width = 2f))
        drawCircle(gray.edge, radius = cfg.coreR + 4, center = Offset(0f, 2f), style = Stroke(width = 2f))
        val crack = Path().apply {
            moveTo(-4f, -cfg.torsoH / 2 + 4f)
            lineTo(2f, -8f); lineTo(-3f, 0f); lineTo(4f, 10f); lineTo(0f, cfg.torsoH / 2 - 4f)
        }
        drawPath(crack, Color(0xFF2A323A), style = Stroke(width = 2f))
    }

    // legs sprawled to the right
    drawLimb(Offset(214f, groundY - 8f), cfg.legW, cfg.legH, 1.35f, moodColor(PetMood.DEAD), gray)
    drawLimb(Offset(228f, groundY - 10f), cfg.legW, cfg.legH, 1.9f, moodColor(PetMood.DEAD), gray)
    // one arm flopped forward, one buried under
    drawLimb(Offset(128f, groundY - cfg.torsoH + 8f), cfg.armW, cfg.armH, -2.4f, moodColor(PetMood.DEAD), gray)
    drawLimb(Offset(206f, groundY - cfg.torsoH + 4f), cfg.armW, cfg.armH, 2.6f, moodColor(PetMood.DEAD), gray)

    // head resting on the ground, tipped sideways
    withTransform({
        translate(88f, groundY - cfg.headR + 6f)
        rotate(-24f)
    }) {
        drawCircle(
            brush = Brush.radialGradient(listOf(gray.light, gray.body, gray.mid),
                center = Offset(-cfg.headR * 0.3f, -cfg.headR * 0.4f), radius = cfg.headR * 1.9f),
            radius = cfg.headR, center = Offset.Zero
        )
        drawCircle(gray.edge, radius = cfg.headR, center = Offset.Zero, style = Stroke(width = 2.5f))
        // split crack across the shell
        val split = Path().apply {
            moveTo(-cfg.headR * 0.5f, -cfg.headR * 0.75f)
            lineTo(-cfg.headR * 0.2f, -cfg.headR * 0.3f)
            lineTo(-cfg.headR * 0.45f, cfg.headR * 0.1f)
        }
        drawPath(split, Color(0xFF2A323A), style = Stroke(width = 2f))
        // dim visor with X eyes
        val vw = cfg.headR * 1.4f
        val vh = cfg.headR * 0.9f
        drawRoundRect(gray.visor, topLeft = Offset(-vw / 2, -vh / 2 + 3f), size = Size(vw, vh), cornerRadius = CornerRadius(vh * 0.34f))
        val strokeX = Stroke(width = 3f, cap = StrokeCap.Round)
        listOf(-1f, 1f).forEach { side ->
            val ex = side * vw * 0.22f
            val ey = 2f
            drawLine(Color(0xFF8FA0AE), Offset(ex - 6f, ey - 6f), Offset(ex + 6f, ey + 6f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(Color(0xFF8FA0AE), Offset(ex + 6f, ey - 6f), Offset(ex - 6f, ey + 6f), strokeWidth = 3f, cap = StrokeCap.Round)
        }
        drawArc(Color(0xFF8FA0AE), 180f, 180f, useCenter = false, topLeft = Offset(-6f, 10f), size = Size(12f, 8f), style = strokeX)
    }

    // bent antenna lying flat
    drawLine(gray.edge, Offset(86f, groundY - cfg.headR * 1.5f), Offset(64f, groundY - cfg.headR * 1.9f), strokeWidth = 3f, cap = StrokeCap.Round)
    drawLine(gray.edge, Offset(64f, groundY - cfg.headR * 1.9f), Offset(48f, groundY - cfg.headR * 1.55f), strokeWidth = 3f, cap = StrokeCap.Round)
    drawCircle(Color(0xFF454F58), radius = 4f, center = Offset(48f, groundY - cfg.headR * 1.55f))

    drawTombstone(258f, groundY + 4f, 1f)
    drawGhost(150f, groundY - cfg.torsoH - cfg.headR - 46f)

    // cold dust motes
    listOf(Triple(120f, groundY - 120f, 2.2f), Triple(210f, groundY - 150f, 1.6f), Triple(96f, groundY - 60f, 1.4f),
        Triple(238f, groundY - 70f, 1.8f)).forEach { (x, y, r) ->
        drawCircle(Color(0xFF8FA0AE).copy(alpha = 0.35f), radius = r, center = Offset(x, y))
    }
}

private fun DrawScope.drawDeadEgg() {
    val path = Path().apply {
        moveTo(0f, -62f)
        cubicTo(34f, -62f, 40f, -10f, 34f, 30f)
        cubicTo(24f, 58f, -24f, 58f, -34f, 30f)
        cubicTo(-40f, -10f, -34f, -62f, 0f, -62f)
        close()
    }
    drawPath(path, brush = Brush.linearGradient(
        listOf(Color(0xFFB9C3CC), Color(0xFF97A4B0), Color(0xFF75828F)), start = Offset(-40f, -62f), end = Offset(40f, 58f)
    ))
    drawPath(path, color = Color(0xFF39434C), style = Stroke(width = 3f))
    val crack = Path().apply {
        moveTo(-10f, -50f); lineTo(-2f, -26f); lineTo(-14f, -8f); lineTo(-4f, 16f); lineTo(-12f, 40f)
    }
    drawPath(crack, Color(0xFF2A323A), style = Stroke(width = 2.5f))
    drawOval(Color(0xFF101418), topLeft = Offset(-15f, -34f), size = Size(30f, 22f))
    listOf(-1f, 1f).forEach { side ->
        val ex = side * 7f
        drawLine(Color(0xFF8FA0AE), Offset(ex - 4f, -30f), Offset(ex + 4f, -20f), strokeWidth = 2.5f, cap = StrokeCap.Round)
        drawLine(Color(0xFF8FA0AE), Offset(ex + 4f, -30f), Offset(ex - 4f, -20f), strokeWidth = 2.5f, cap = StrokeCap.Round)
    }
    drawGhost(46f, -96f)
}

private fun DrawScope.drawTombstone(cx: Float, groundY: Float, scaleF: Float) {
    withTransform({ translate(cx, groundY); scale(scaleF, scaleF, pivot = Offset.Zero); rotate(-4f, pivot = Offset(0f, 0f)) }) {
        val stone = Path().apply {
            moveTo(-24f, 0f)
            lineTo(-24f, -52f)
            arcTo(Rect(-24f, -80f, 24f, -32f), startAngleDegrees = 180f, sweepAngleDegrees = 180f, forceMoveTo = false)
            lineTo(24f, 0f)
            close()
        }
        drawPath(stone, brush = Brush.linearGradient(
            listOf(Color(0xFF8B98A5), Color(0xFF6B7A88), Color(0xFF55636F)),
            start = Offset(-24f, -80f), end = Offset(24f, 0f)
        ))
        drawPath(stone, color = Color(0xFF3E4B57), style = Stroke(width = 2.5f))
        // bevel
        val bevel = Path().apply {
            moveTo(-16f, -4f)
            lineTo(-16f, -50f)
            arcTo(Rect(-16f, -70f, 16f, -38f), startAngleDegrees = 180f, sweepAngleDegrees = 180f, forceMoveTo = false)
            lineTo(16f, -4f)
            close()
        }
        drawPath(bevel, color = Color(0xFF7A8896).copy(alpha = 0.6f), style = Stroke(width = 1.5f))
        // engraved cross + R.I.P.
        drawLine(Color(0xFF37424D), Offset(0f, -62f), Offset(0f, -40f), strokeWidth = 3f)
        drawLine(Color(0xFF37424D), Offset(-7f, -55f), Offset(7f, -55f), strokeWidth = 3f)
        drawTextNative("R.I.P.", 0f, -20f, 12f, Color(0xFF37424D))
        // soil mound
        drawOval(Color(0xFF3A2A20).copy(alpha = 0.8f), topLeft = Offset(-32f, -6f), size = Size(64f, 10f))
    }
}

private fun DrawScope.drawGhost(cx: Float, cy: Float) {
    withTransform({ translate(cx, cy); rotate(-6f) }) {
        val body = Path().apply {
            arcTo(Rect(-18f, -26f, 18f, 10f), startAngleDegrees = 180f, sweepAngleDegrees = 180f, forceMoveTo = false)
            lineTo(18f, 16f)
            quadraticBezierTo(12f, 10f, 9f, 18f)
            quadraticBezierTo(4f, 10f, 0f, 20f)
            quadraticBezierTo(-4f, 10f, -9f, 18f)
            quadraticBezierTo(-12f, 10f, -18f, 16f)
            close()
        }
        drawCircle(
            brush = Brush.radialGradient(listOf(Color.White.copy(alpha = 0.22f), Color.Transparent), center = Offset.Zero, radius = 34f),
            radius = 34f, center = Offset.Zero
        )
        drawPath(body, Color(0xFFEDF3F8).copy(alpha = 0.75f))
        drawPath(body, Color(0xFFB9C6D2).copy(alpha = 0.5f), style = Stroke(width = 1.2f))
        listOf(-1f, 1f).forEach { side ->
            drawOval(Color(0xFF2A323A).copy(alpha = 0.85f), topLeft = Offset(side * 7f - 2.6f, -14f), size = Size(5.2f, 7f))
        }
        drawOval(Color(0xFF2A323A).copy(alpha = 0.6f), topLeft = Offset(-2.5f, -3f), size = Size(5f, 6f))
    }
}

private fun DrawScope.drawMoodEffect(mood: PetMood, accent: Color, cy: Float, t: Float) {
    val sparkColors = listOf(Color(0xFFF0B84C), Color(0xFF4DE0C4), Color(0xFFF4C94D), Color(0xFF5AA9E6))
    when (mood) {
        PetMood.HUNGRY -> {
            val r = 76f + sin(t * 0.15f) * 5f
            drawCircle(accent.copy(alpha = 0.3f), radius = r, center = Offset(180f, cy + 55f), style = Stroke(width = 2f))
            // tiny floating bowl of bytes
            withTransform({ translate(248f, cy + 10f + sin(t * 0.09f) * 4f) }) {
                drawArc(Color(0xFF9FB6CB), 0f, 180f, useCenter = true, topLeft = Offset(-10f, -4f), size = Size(20f, 16f))
                drawLine(Color(0xFF5C7288), Offset(-12f, -4f), Offset(12f, -4f), strokeWidth = 2f, cap = StrokeCap.Round)
                drawTextNative("01", 0f, -6f, 8f, accent.copy(alpha = 0.8f))
            }
        }
        PetMood.SAD -> {
            drawOval(accent, topLeft = Offset(195f, cy - 22f + (t % 30f)), size = Size(6f, 10f))
            drawOval(accent.copy(alpha = 0.6f), topLeft = Offset(165f, cy - 14f + ((t + 15f) % 30f)), size = Size(5f, 8f))
        }
        PetMood.EXCITED -> {
            for (i in 0 until 6) {
                val a = t * 0.07f + i * 1.05f
                drawCircle(sparkColors[i % sparkColors.size], radius = 2.6f,
                    center = Offset(180f + cos(a) * 70f, cy - 40f + sin(a) * 34f))
            }
            for (i in 0 until 3) {
                val a = -t * 0.05f + i * 2.1f
                drawStar(180f + cos(a) * 58f, cy - 6f + sin(a) * 26f, 4f, sparkColors[(i + 1) % sparkColors.size])
            }
        }
        PetMood.SICK -> translate(219f, cy - 25f + sin(t * 0.06f) * 5f) {
            drawPath(Path().apply {
                moveTo(0f, -8f)
                cubicTo(2f, -3f, 7f, 1f, 5f, 5f)
                cubicTo(2f, 10f, -6f, 7f, -5f, 2f)
                close()
            }, Color(0xFF52E07A))
            drawPath(Path().apply {
                moveTo(16f, -2f)
                cubicTo(18f, 2f, 22f, 5f, 20f, 9f)
                cubicTo(18f, 13f, 12f, 11f, 13f, 6f)
                close()
            }, Color(0xFF52E07A).copy(alpha = 0.6f))
        }
        PetMood.SLEEPING -> {
            // rising "z z Z"
            val cycle = (t * 0.35f) % 3f
            for (i in 0 until 3) {
                val p = ((cycle - i) + 3f) % 3f / 3f
                val alpha = (0.9f - p * 0.7f).coerceIn(0f, 1f)
                drawTextNative(if (i == 2) "Z" else "z", 236f + i * 8f + p * 14f, cy - 30f - p * 40f - i * 4f, 12f + i * 5f, Color(0xFFA9C7E8).copy(alpha = alpha))
            }
        }
        else -> Unit
    }
}
