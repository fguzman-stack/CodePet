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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.tamagotchi.code.R
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

private object Metal {
    val base = Color(0xFFDBE2E8)
    val mid = Color(0xFFAAB6C0)
    val edge = Color(0xFF5B6B78)
    val dark = Color(0xFF39434C)
    val visor = Color(0xFF0B1420)
}

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

private val STAGE_CONFIG = mapOf(
    PetEvolutionStage.Child to StageConfig(28f, 56f, 48f, 13f, 30f, 15f, 22f, 1, false, false, false, 8f, 200f),
    PetEvolutionStage.Adult to StageConfig(32f, 66f, 60f, 15f, 42f, 18f, 34f, 1, false, false, false, 11f, 195f),
    PetEvolutionStage.Veteran to StageConfig(34f, 72f, 66f, 17f, 46f, 20f, 38f, 2, true, false, false, 13f, 192f),
    PetEvolutionStage.Legendary to StageConfig(36f, 76f, 70f, 18f, 48f, 21f, 40f, 3, true, true, true, 15f, 188f)
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
        rotate(if (failed && animate) sin(t * 0.8f) * 5f else 0f) {
            drawCodeyFrame(stage, if (celebrating && animate) PetMood.EXCITED else mood, t)
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

    if (stage == PetEvolutionStage.Egg) {
        drawShadow(46f, 180f, 292f)
        rotate(if (mood == PetMood.DEAD) 86f else 0f, Offset(180f, 210f)) {
            drawEgg(t, accent, mood != PetMood.DEAD)
        }
        if (mood == PetMood.DEAD) drawMoodEffect(mood, accent, 180f, t)
        return
    }

    val cfg = STAGE_CONFIG.getValue(stage)

    var bounce = 0f
    var legSwing = 0f
    var armSwing = 0f
    var tilt = 0f

    if (mood == PetMood.DEAD) {
        tilt = (PI / 2.1).toFloat()
    } else {
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
    }

    drawShadow(cfg.torsoW / 1.6f, 180f, 300f + (cfg.baseY - 200f))
    val cy = drawRobot(mood, cfg, accent, bounce, legSwing, armSwing, tilt, t, blinking)
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

private fun DrawScope.metalBrush(w: Float, h: Float, cx: Float, cy: Float): Brush =
    Brush.linearGradient(
        colors = listOf(Metal.base, Metal.mid, Metal.edge),
        start = Offset(cx - w / 2, cy - h / 2),
        end = Offset(cx + w / 2, cy + h / 2)
    )

private fun DrawScope.drawEgg(t: Float, accent: Color, powered: Boolean) {
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
        drawPath(path, brush = metalBrush(80f, 120f, 0f, -10f))
        drawPath(path, color = Metal.edge, style = Stroke(width = 3f))

        drawLine(accent.copy(alpha = 0.75f), Offset(-35f, -4f), Offset(35f, -4f), strokeWidth = 2.5f)

        drawOval(Metal.visor, topLeft = Offset(-14f, -30f), size = Size(28f, 20f))
        val pulse = if (powered) 0.5f + sin(t * 0.06f) * 0.2f else 0f
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(accent.copy(alpha = pulse), Color.Transparent),
                center = Offset(0f, -20f), radius = 14f
            ),
            topLeft = Offset(-14f, -30f), size = Size(28f, 20f)
        )

        listOf(Offset(-20f, 20f), Offset(20f, 20f), Offset(0f, 44f)).forEach {
            drawCircle(Metal.dark.copy(alpha = 0.5f), radius = 3f, center = it)
        }
    }
}

private fun DrawScope.drawLimb(pivot: Offset, w: Float, h: Float, angle: Float, accent: Color) {
    withTransform({
        translate(pivot.x, pivot.y)
        rotate(degrees = Math.toDegrees(angle.toDouble()).toFloat(), pivot = Offset.Zero)
    }) {
        val path = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    Offset(-w / 2, 0f).x, Offset(-w / 2, 0f).y, Offset(-w / 2, 0f).x + w, h,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w / 2, w / 2)
                )
            )
        }
        drawPath(path, brush = metalBrush(w, h, 0f, h / 2))
        drawPath(path, color = Metal.edge, style = Stroke(width = 2f))

        drawCircle(Metal.dark, radius = w / 2 + 2, center = Offset.Zero)
        drawCircle(accent.copy(alpha = 0.55f), radius = w / 2 - 1, center = Offset.Zero)
    }
}

private fun DrawScope.drawAntenna(x: Float, topY: Float, len: Float, accent: Color, sway: Float, powered: Boolean) {
    withTransform({
        translate(x, topY)
        rotate(degrees = Math.toDegrees(sway.toDouble()).toFloat(), pivot = Offset.Zero)
    }) {
        drawLine(Metal.edge, Offset.Zero, Offset(0f, -len), strokeWidth = 3f, cap = StrokeCap.Round)
        if (powered) drawCircle(
            brush = Brush.radialGradient(listOf(accent, Color.Transparent), center = Offset(0f, -len), radius = 9f),
            radius = 9f, center = Offset(0f, -len)
        )
        drawCircle(accent, radius = 4f, center = Offset(0f, -len))
    }
}

private fun DrawScope.drawWing(x: Float, y: Float, flip: Float, accent: Color) {
    withTransform({
        translate(x, y)
        scale(flip, 1f, pivot = Offset.Zero)
    }) {
        val path = Path().apply {
            moveTo(0f, 0f); lineTo(34f, -10f); lineTo(40f, 8f); lineTo(30f, 30f); lineTo(6f, 24f); close()
        }
        drawPath(path, brush = metalBrush(40f, 30f, 20f, 10f))
        drawPath(path, color = accent.copy(alpha = 0.8f), style = Stroke(width = 1.5f))
    }
}

private fun DrawScope.drawCore(cx: Float, cy: Float, r: Float, mood: PetMood, accent: Color, t: Float) {
    if (mood != PetMood.DEAD) {
        val pulse = 0.75f + sin(t * if (mood == PetMood.EXCITED) 0.25f else 0.08f) * 0.25f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(accent.copy(alpha = 0.35f * pulse), Color.Transparent),
                center = Offset(cx, cy), radius = r * 2.4f
            ),
            radius = r * 2.4f, center = Offset(cx, cy)
        )
    }
    drawCircle(if (mood == PetMood.DEAD) Color(0xFF454F58) else accent, radius = r, center = Offset(cx, cy))
    drawCircle(Metal.dark, radius = r, center = Offset(cx, cy), style = Stroke(width = 2f))
    drawCircle(Metal.edge, radius = r + 4, center = Offset(cx, cy), style = Stroke(width = 2f))
}

private fun DrawScope.drawVisorFace(cx: Float, cy: Float, w: Float, h: Float, mood: PetMood, accent: Color, blinking: Boolean) {
    val topLeft = Offset(cx - w / 2, cy - h / 2)
    drawRoundRect(Metal.visor, topLeft = topLeft, size = Size(w, h), cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.32f))
    for (line in 1 until (h / 4f).toInt()) {
        val y = topLeft.y + line * 4f
        drawLine(Color.White.copy(alpha = 0.035f), Offset(topLeft.x + 4f, y), Offset(topLeft.x + w - 4f, y), 0.5f)
    }

    val eyeGap = w * 0.26f
    val eyeY = cy - 6f
    val closed = blinking && mood != PetMood.DEAD && mood != PetMood.SLEEPING && mood != PetMood.EXCITED
    val stroke = Stroke(width = 3f, cap = StrokeCap.Round)

    listOf(-1f, 1f).forEach { side ->
        val ex = cx + side * eyeGap
        if (mood != PetMood.DEAD) {
            drawCircle(Brush.radialGradient(listOf(accent.copy(alpha = 0.22f), Color.Transparent), Offset(ex, eyeY), 10f), 10f, Offset(ex, eyeY))
        }
        when {
            mood == PetMood.DEAD -> {
                drawLine(Metal.edge, Offset(ex - 5, eyeY - 5), Offset(ex + 5, eyeY + 5), strokeWidth = 3f)
                drawLine(Metal.edge, Offset(ex + 5, eyeY - 5), Offset(ex - 5, eyeY + 5), strokeWidth = 3f)
            }
            mood == PetMood.SLEEPING || closed -> {
                drawLine(accent, Offset(ex - 6, eyeY), Offset(ex + 6, eyeY), strokeWidth = 3f, cap = StrokeCap.Round)
            }
            mood == PetMood.SICK -> {
                drawLine(accent, Offset(ex - 5, eyeY - 5), Offset(ex + 5, eyeY + 5), strokeWidth = 3f)
                drawLine(accent, Offset(ex + 5, eyeY - 5), Offset(ex - 5, eyeY + 5), strokeWidth = 3f)
            }
            mood == PetMood.STUDYING -> {
                val p = Path().apply { moveTo(ex + 4, eyeY - 5); lineTo(ex - 3, eyeY); lineTo(ex + 4, eyeY + 5) }
                drawPath(p, accent, style = stroke)
            }
            mood == PetMood.EXCITED -> drawStar(ex, eyeY, 6f, accent)
            mood == PetMood.SAD -> {
                drawArc(accent, startAngle = 200f, sweepAngle = 140f, useCenter = false,
                    topLeft = Offset(ex - 5, eyeY - 2), size = Size(10f, 10f), style = stroke)
            }
            mood == PetMood.HUNGRY -> drawCircle(accent, radius = 5f, center = Offset(ex, eyeY))
            else -> drawArc(accent, startAngle = 180f, sweepAngle = 180f, useCenter = false,
                topLeft = Offset(ex - 5, eyeY - 2), size = Size(10f, 10f), style = stroke)
        }
    }

    if (mood != PetMood.DEAD) {
        drawCircle(Brush.radialGradient(listOf(accent.copy(alpha = 0.18f), Color.Transparent), Offset(cx, eyeY + 12f), 8f), 8f, Offset(cx, eyeY + 12f))
    }
    when (mood) {
        PetMood.EXCITED -> drawArc(accent, 0f, 180f, false, Offset(cx - 6, eyeY + 7), Size(12f, 12f), style = stroke)
        PetMood.SAD -> drawArc(accent, 180f, 180f, false, Offset(cx - 5, eyeY + 13), Size(10f, 10f), style = stroke)
        PetMood.SICK -> drawPath(Path().apply {
            moveTo(cx - 6f, eyeY + 14f)
            cubicTo(cx - 3f, eyeY + 8f, cx, eyeY + 20f, cx + 3f, eyeY + 14f)
            quadraticTo(cx + 5f, eyeY + 10f, cx + 7f, eyeY + 14f)
        }, accent, style = stroke)
        PetMood.HUNGRY -> drawOval(accent, topLeft = Offset(cx - 4, eyeY + 9), size = Size(8f, 10f))
        PetMood.DEAD -> drawLine(Metal.edge, Offset(cx - 5, eyeY + 14), Offset(cx + 5, eyeY + 14), strokeWidth = 3f)
        PetMood.SLEEPING -> drawLine(accent, Offset(cx - 4, eyeY + 14), Offset(cx + 4, eyeY + 14), strokeWidth = 3f)
        else -> drawArc(accent, 0f, 180f, false, Offset(cx - 5, eyeY + 5), Size(10f, 10f), style = stroke)
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
    mood: PetMood, cfg: StageConfig, accent: Color,
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
        val hipY = torsoBottom - 4
        val shoulderX = cfg.torsoW / 2 - 4
        val hipX = cfg.torsoW / 2 - 8

        if (cfg.halo && mood != PetMood.DEAD) {
            val hy = torsoTop - cfg.headR * 2 - 14
            drawArc(
                accent.copy(alpha = (0.7f + sin(t * 0.05f) * 0.2f).coerceIn(0f, 1f)),
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(-26f, hy - 7f), size = Size(52f, 14f), style = Stroke(width = 3f)
            )
        }
        if (cfg.wings) {
            drawWing(-shoulderX - 4, shoulderY + 6, -1f, accent)
            drawWing(shoulderX + 4, shoulderY + 6, 1f, accent)
        }

        drawLimb(Offset(-hipX + 10, hipY), cfg.legW, cfg.legH, legSwing, accent)
        drawLimb(Offset(hipX - 10, hipY), cfg.legW, cfg.legH, -legSwing, accent)

        drawRoundRect(
            brush = metalBrush(cfg.torsoW, cfg.torsoH, 0f, 0f),
            topLeft = Offset(-cfg.torsoW / 2, torsoTop),
            size = Size(cfg.torsoW, cfg.torsoH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f)
        )
        drawLine(Metal.dark.copy(alpha = 0.4f), Offset(0f, torsoTop + 6), Offset(0f, torsoBottom - 6), strokeWidth = 1.5f)
        listOf(
            Offset(-cfg.torsoW / 2 + 8, torsoTop + 8), Offset(cfg.torsoW / 2 - 8, torsoTop + 8),
            Offset(-cfg.torsoW / 2 + 8, torsoBottom - 8), Offset(cfg.torsoW / 2 - 8, torsoBottom - 8)
        ).forEach { drawCircle(Metal.dark.copy(alpha = 0.5f), radius = 2.5f, center = it) }

        drawCore(0f, 4f, cfg.coreR, mood, accent, t)

        if (cfg.shoulderArmor) {
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFFFE9A8), Color(0xFFE0A83C)), center = Offset(cfg.torsoW / 2 - 16, torsoTop + 18), radius = 6f),
                radius = 6f, center = Offset(cfg.torsoW / 2 - 14, torsoTop + 20)
            )
        }

        drawLimb(Offset(-shoulderX, shoulderY), cfg.armW, cfg.armH, armSwing, accent)
        drawLimb(Offset(shoulderX, shoulderY), cfg.armW, cfg.armH, -armSwing, accent)

        if (cfg.shoulderArmor) {
            listOf(-1f, 1f).forEach { s ->
                drawOval(
                    brush = metalBrush(24f, 18f, s * shoulderX, shoulderY - 6),
                    topLeft = Offset(s * shoulderX - 12, shoulderY - 6 - 9), size = Size(24f, 18f)
                )
                drawOval(
                    accent.copy(alpha = 0.6f),
                    topLeft = Offset(s * shoulderX - 12, shoulderY - 6 - 9), size = Size(24f, 18f),
                    style = Stroke(width = 1.5f)
                )
            }
        }

        val headY = torsoTop - cfg.headR - 6
        drawCircle(brush = metalBrush(cfg.headR * 2, cfg.headR * 2, 0f, headY), radius = cfg.headR, center = Offset(0f, headY))
        drawCircle(Metal.edge, radius = cfg.headR, center = Offset(0f, headY), style = Stroke(width = 2.5f))

        val antTopY = headY - cfg.headR + 4
        for (i in 0 until cfg.antennas) {
            val spread = 16f
            val ax = -((cfg.antennas - 1) * spread) / 2 + i * spread
            val extraLen = if (i == cfg.antennas / 2) 6f else 0f
            drawAntenna(ax, antTopY, 20f + extraLen, accent,
                if (mood == PetMood.DEAD) 0f else sin(t * 0.04f + i) * 0.08f,
                powered = mood != PetMood.DEAD)
        }

        drawVisorFace(0f, headY + 2f, cfg.headR * 1.15f, cfg.headR * 0.95f, mood, accent, blinking)
    }

    return cy
}

private fun DrawScope.drawMoodEffect(mood: PetMood, accent: Color, cy: Float, t: Float) {
    val sparkColors = listOf(Color(0xFFF0B84C), Color(0xFF4DE0C4), Color(0xFFF4C94D), Color(0xFF5AA9E6))
    when (mood) {
        PetMood.HUNGRY -> {
            val r = 76f + sin(t * 0.15f) * 5f
            drawCircle(accent.copy(alpha = 0.3f), radius = r, center = Offset(180f, cy + 55f), style = Stroke(width = 2f))
        }
        PetMood.SAD -> drawOval(accent, topLeft = Offset(195f, cy - 22f + (t % 30f)), size = Size(6f, 10f))
        PetMood.EXCITED -> {
            for (i in 0 until 6) {
                val a = t * 0.07f + i * 1.05f
                drawCircle(sparkColors[i % sparkColors.size], radius = 2.6f,
                    center = Offset(180f + cos(a) * 70f, cy - 40f + sin(a) * 34f))
            }
        }
        PetMood.SICK -> translate(219f, cy - 25f + sin(t * 0.06f) * 5f) {
            drawPath(Path().apply {
                moveTo(0f, -8f)
                cubicTo(2f, -3f, 7f, 1f, 5f, 5f)
                cubicTo(2f, 10f, -6f, 7f, -5f, 2f)
                close()
            }, Color(0xFF52E07A))
        }
        PetMood.DEAD -> {
            drawRect(Color(0xFF7C8B96), topLeft = Offset(107f, 160f), size = Size(26f, 40f))
            drawArc(Color(0xFF7C8B96), 180f, 180f, true, Offset(107f, 147f), Size(26f, 26f))
        }
        else -> Unit
    }
}
