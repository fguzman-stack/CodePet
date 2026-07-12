package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.ui.theme.AppTheme
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.abs
import kotlin.math.PI
import kotlin.random.Random

@Composable
fun AnimatedThemeBackground(
    theme: AppTheme,
    reduceMotion: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgModifier = if (theme.usesGradients && theme.gradientColors.size > 1) {
        Modifier.background(brush = Brush.verticalGradient(theme.gradientColors))
    } else {
        Modifier.background(color = theme.background)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(bgModifier)
    ) {
        if (!reduceMotion) {
            when (theme.name) {
                "Matrix Green" -> MatrixBackground(theme.primary, theme.accent)
                "Galáctico" -> GalacticBackground()
                "Cyberpunk" -> CyberpunkBackground(theme.primary, theme.secondary, theme.tertiary)
                "Sakura" -> SakuraBackground()
                "Minimalista" -> MinimalistBackground()
                "Neón" -> NeonBackground(theme.primary, theme.secondary, theme.tertiary)
                "Océano" -> OceanBackground()
                "Volcánico" -> VolcanicBackground()
                "Samurai" -> SamuraiBackground()
                "Aurora" -> AuroraBackground(theme.primary, theme.secondary, theme.tertiary)
                "Nocturno" -> NightBackground()
                "Retro Pixel" -> RetroPixelBackground()
            }
        }
    }
}

// ---------------------------------------------------------
// 1. Matrix Green — 1/0 rain with hidden pet messages & speed bursts
// ---------------------------------------------------------
@Composable
fun MatrixBackground(primary: Color, accent: Color) {
    val textMeasurer = rememberTextMeasurer()
    val infiniteTransition = rememberInfiniteTransition(label = "matrix")

    val baseTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(320000, easing = LinearEasing)),
        label = "baseTime"
    )

    val burstFactor by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 320000
                0f at 0 with LinearEasing
                0f at 60000 with LinearEasing
                1f at 84000 with FastOutSlowInEasing
                1f at 104000 with LinearEasing
                0f at 128000 with LinearEasing
                0f at 320000 with LinearEasing
            }
        ),
        label = "burst"
    )

    val hiddenMessages = remember {
        listOf(
            "te quiero",
            "graba esto",
            "come sano",
            "sigue asi",
            "eres genial",
            ":)",
            "debug mode",
            "hola mundo",
            "push it",
            "commit"
        )
    }

    val msgColumn = remember { Random.nextInt(3, 8) }
    val msgIndex = remember { Random.nextInt(hiddenMessages.size) }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val numColumns = (size.width / 32f).toInt()
        val random = java.util.Random(42)
        val speedMultiplier = 1f + burstFactor * 0.5f
        val textStyle = TextStyle(
            color = primary,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )
        val dimStyle = TextStyle(
            color = primary.copy(alpha = 0.3f),
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )

        for (i in 0 until numColumns) {
            val speed = (8f + random.nextFloat() * 12f) * speedMultiplier
            val yOffset = (baseTime * speed + random.nextFloat() * 2000f) % (size.height + 200f) - 100f
            val length = 6 + random.nextInt(6)

            for (j in 0 until length) {
                val alpha = 1f - (j.toFloat() / length)
                val char = if (random.nextFloat() > 0.5f) "1" else "0"
                val charStyle = if (j == 0) textStyle else dimStyle
                val y = yOffset - j * 24f
                if (y in -24f..size.height - 16f) {
                    drawText(
                        textMeasurer,
                        text = char,
                        topLeft = Offset(i * 32f + 8f, y.coerceAtLeast(0f)),
                        style = charStyle.copy(
                            color = charStyle.color.copy(
                                alpha = alpha.coerceIn(0.15f, 1f) * if (j == 0) 1f else 0.6f
                            )
                        )
                    )
                }
            }
        }

        if (msgColumn in 0 until numColumns) {
            val revealProgress = ((baseTime % 5000f) / 5000f)
            val msg = hiddenMessages[msgIndex]
            val visibleChars = (revealProgress * msg.length).toInt().coerceIn(0, msg.length)
            if (visibleChars > 0) {
                drawText(
                    textMeasurer,
                    text = msg.substring(0, visibleChars),
                    topLeft = Offset(msgColumn * 32f + 8f, size.height / 3f),
                    style = TextStyle(
                        color = accent,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 2. Galáctico — Nebulae, stardust, deep space
// ---------------------------------------------------------
@Composable
fun GalacticBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(60000, easing = LinearEasing)),
        label = "time"
    )

    val nebulaColors = remember {
        listOf(
            listOf(Color(0xFF6C2BD9).copy(alpha = 0.12f), Color.Transparent),
            listOf(Color(0xFFE040FB).copy(alpha = 0.08f), Color.Transparent),
            listOf(Color(0xFF1A237E).copy(alpha = 0.10f), Color.Transparent),
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(100)
        val cx = size.width / 2f
        val cy = size.height / 2f

        nebulaColors.forEachIndexed { index, colors ->
            drawCircle(
                brush = Brush.radialGradient(colors),
                radius = size.width * 0.7f,
                center = Offset(
                    cx + sin(time * 0.3f + index * 2f) * size.width * 0.25f,
                    cy + cos(time * 0.4f + index * 2.5f) * size.height * 0.2f
                )
            )
        }

        for (i in 0..120) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val r = random.nextFloat() * 2.5f + 0.5f
            val phase = random.nextFloat() * 2f * PI.toFloat()
            val alpha = (sin(time * 0.7f + phase) + 1f) / 2f * 0.7f + 0.2f
            val starColor = when {
                random.nextFloat() > 0.8f -> Color(0xFFFFE0B2)
                random.nextFloat() > 0.6f -> Color(0xFFB3E5FC)
                else -> Color.White
            }
            drawCircle(starColor.copy(alpha = alpha), radius = r, center = Offset(x, y))
        }

        val sparkleRandom = java.util.Random(200)
        for (i in 0..8) {
            val sx = sparkleRandom.nextFloat() * size.width
            val sy = sparkleRandom.nextFloat() * size.height
            val sparkleAlpha = (sin(time * 1.2f + sparkleRandom.nextFloat() * 10f) + 1f) / 2f
            drawCircle(
                color = Color.White.copy(alpha = sparkleAlpha * 0.9f),
                radius = 1.5f + sparkleAlpha * 2f,
                center = Offset(sx, sy)
            )
        }

        for (i in 0..40) {
            val dx = random.nextFloat() * size.width
            val dy = random.nextFloat() * size.height
            val dotAlpha = random.nextFloat() * 0.3f
            drawCircle(Color.White.copy(alpha = dotAlpha), radius = 0.5f, center = Offset(dx, dy))
        }
    }
}

// ---------------------------------------------------------
// 3. Cyberpunk (Improved grid + glitch lines)
// ---------------------------------------------------------
@Composable
fun CyberpunkBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyberpunk")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing)),
        label = "grid"
    )
    val glitchPhase by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(24000, easing = LinearEasing)),
        label = "glitch"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val spacing = 80f

        for (i in 0..(size.width / spacing).toInt() + 1) {
            drawLine(
                color = color2.copy(alpha = 0.2f),
                start = Offset(i * spacing, 0f),
                end = Offset(i * spacing, size.height),
                strokeWidth = 1.5f
            )
        }

        var y = offset
        while (y < size.height) {
            drawLine(
                color = color1.copy(alpha = 0.35f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
            y += spacing
        }

        val glitchCount = (sin(glitchPhase * PI.toFloat() * 4f) * 3f + 4f).toInt()
        val glitchRandom = java.util.Random(99)
        for (g in 0 until glitchCount) {
            val gy = glitchRandom.nextFloat() * size.height
            val gx = glitchRandom.nextFloat() * size.width * 0.5f
            val gw = 20f + glitchRandom.nextFloat() * 80f
            drawRect(
                color = color3.copy(alpha = 0.15f),
                topLeft = Offset(gx, gy),
                size = Size(gw, 2f)
            )
        }
    }
}

// ---------------------------------------------------------
// 4. Sakura — Slow, calm falling petals
// ---------------------------------------------------------
@Composable
fun SakuraBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "sakura")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(140000, easing = LinearEasing)),
        label = "fall"
    )

    val petalColors = listOf(
        Color(0xFFFFB7C5),
        Color(0xFFFFCDD6),
        Color(0xFFFFA0B4),
        Color(0xFFF8BBD0),
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(200)
        for (i in 0..25) {
            val speedY = 10f + random.nextFloat() * 18f
            val speedX = 4f + random.nextFloat() * 8f
            val phaseX = random.nextFloat() * 2f * PI.toFloat()
            val phaseRot = random.nextFloat() * 2f * PI.toFloat()

            val yOffset = (time * speedY + random.nextFloat() * 3000f) % (size.height + 150f) - 75f
            val xOffset = (random.nextFloat() * (size.width + 100f) - 50f) +
                    sin(time * 0.05f + phaseX) * speedX * 3f

            withTransform({
                translate(xOffset.toFloat(), yOffset)
                rotate((time * (3f + random.nextFloat() * 5f) + phaseRot * 50f) % 360f)
            }) {
                val petalColor = petalColors[random.nextInt(petalColors.size)]
                drawOval(
                    color = petalColor.copy(alpha = 0.5f),
                    topLeft = Offset(-6f, -10f),
                    size = Size(12f, 20f)
                )
                drawOval(
                    color = petalColor.copy(alpha = 0.3f),
                    topLeft = Offset(-4f, -8f),
                    size = Size(8f, 16f)
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 5. Minimalista (refined — cleaner, softer)
// ---------------------------------------------------------
@Composable
fun MinimalistBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "minimal")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(90000, easing = LinearEasing)),
        label = "orbit"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Black.copy(alpha = 0.025f),
            radius = size.width * 0.6f,
            center = Offset(
                size.width / 2 + sin(time * 0.7f) * 80f,
                size.height / 3 + cos(time * 0.7f) * 80f
            )
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.02f),
            radius = size.width * 0.5f,
            center = Offset(
                size.width / 2 + cos(time * 0.5f + 1f) * 120f,
                size.height / 1.5f + sin(time * 0.5f + 1f) * 120f
            )
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.015f),
            radius = size.width * 0.35f,
            center = Offset(
                size.width / 2 + sin(time * 0.3f + 2f) * 160f,
                size.height / 2f + cos(time * 0.3f + 2f) * 100f
            )
        )
    }
}

// ---------------------------------------------------------
// 6. Neón — Multiple glowing rings + pulsing
// ---------------------------------------------------------
@Composable
fun NeonBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon")
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(15000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse1"
    )
    val pulse2 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(22000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse2"
    )
    val pulse3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(17000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse3"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = 0.08f * pulse1), color2.copy(alpha = 0.15f * pulse1)),
                center = Offset(cx, cy),
                radius = size.width
            ),
            size = size
        )

        drawCircle(
            color = color1.copy(alpha = 0.12f * pulse2),
            radius = size.width * 0.15f,
            center = Offset(cx - size.width * 0.2f + sin(pulse3 * 3f) * 30f, cy - size.height * 0.15f)
        )

        drawCircle(
            color = color2.copy(alpha = 0.10f * pulse3),
            radius = size.width * 0.1f,
            center = Offset(cx + size.width * 0.25f + cos(pulse2 * 2f) * 20f, cy + size.height * 0.2f)
        )

        drawCircle(
            color = color3.copy(alpha = 0.08f * pulse1),
            radius = size.width * 0.08f,
            center = Offset(cx - size.width * 0.1f, cy + size.height * 0.25f)
        )

        drawCircle(
            color = color1.copy(alpha = 0.15f * pulse3),
            radius = size.width * 0.4f,
            style = Stroke(width = 1.5f),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = color2.copy(alpha = 0.1f * pulse2),
            radius = size.width * 0.3f,
            style = Stroke(width = 1f),
            center = Offset(cx, cy)
        )
        drawCircle(
            color = color3.copy(alpha = 0.12f * pulse1),
            radius = size.width * 0.2f,
            style = Stroke(width = 0.8f),
            center = Offset(cx, cy)
        )
    }
}

// ---------------------------------------------------------
// 7. Océano — Full-screen waves with bubbles
// ---------------------------------------------------------
@Composable
fun OceanBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "ocean")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(70000, easing = LinearEasing)),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val waveColors = listOf(
            Color(0xFF00B4D8),
            Color(0xFF48CAE4),
            Color(0xFF90E0EF),
            Color(0xFF03045E)
        )

        for (w in waveColors.indices) {
            val path = Path()
            val baseY = size.height * (0.5f + w * 0.12f)
            val waveHeight = 30f + w * 12f
            val freq = 80f + w * 30f
            val phase = w * 1.2f

            path.moveTo(0f, size.height)
            for (i in 0..size.width.toInt() step 8) {
                val x = i.toFloat()
                val y = baseY + sin((x / freq) + time * 0.8f + phase) * waveHeight
                path.lineTo(x, y)
            }
            path.lineTo(size.width, size.height)
            path.close()

            drawPath(
                path,
                color = waveColors[w].copy(alpha = 0.25f - w * 0.04f)
            )
        }

        val bubbleRandom = java.util.Random(500)
        for (i in 0..12) {
            val bx = bubbleRandom.nextFloat() * size.width
            val by = (size.height * 0.3f + (time * 20f + bubbleRandom.nextFloat() * 500f) %
                    (size.height * 0.7f))
            val br = 2f + bubbleRandom.nextFloat() * 5f
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = br,
                center = Offset(bx, by),
                style = Stroke(width = 1f)
            )
        }
    }
}

// ---------------------------------------------------------
// 8. Volcánico — Lava flow, embers, glow
// ---------------------------------------------------------
@Composable
fun VolcanicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "volcano")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(90000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val lavaPath = Path()
        val lavaBaseY = size.height * 0.85f
        lavaPath.moveTo(0f, size.height)
        for (i in 0..size.width.toInt() step 6) {
            val x = i.toFloat()
            val y = lavaBaseY + sin((x / 50f) + time * 0.5f) * 20f +
                    sin((x / 30f) + time * 0.3f) * 10f
            lavaPath.lineTo(x, y)
        }
        lavaPath.lineTo(size.width, size.height)
        lavaPath.close()
        drawPath(lavaPath, color = Color(0xFFFF4500).copy(alpha = 0.3f))

        val glowPath = Path()
        glowPath.moveTo(0f, size.height)
        for (i in 0..size.width.toInt() step 6) {
            val x = i.toFloat()
            val y = lavaBaseY + 15f + sin((x / 50f) + time * 0.5f + 1f) * 20f +
                    sin((x / 30f) + time * 0.3f + 1f) * 10f
            glowPath.lineTo(x, y)
        }
        glowPath.lineTo(size.width, size.height)
        glowPath.close()
        drawPath(glowPath, color = Color(0xFFFF8C00).copy(alpha = 0.2f))

        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color.Transparent, Color(0xFFFF4500).copy(alpha = 0.2f)),
                startY = size.height * 0.7f,
                endY = size.height
            ),
            size = size
        )

        val random = java.util.Random(300)
        for (i in 0..50) {
            val speed = 15f + random.nextFloat() * 50f
            val yOffset = size.height + 50f - ((time * speed + random.nextFloat() * 2000f) % (size.height + 100f))
            val xOffset = random.nextFloat() * size.width + sin(time * 0.3f + random.nextFloat() * PI.toFloat()) * 40f
            val r = random.nextFloat() * 5f + 1.5f
            val alpha = ((size.height - yOffset) / size.height).coerceIn(0f, 1f) * 0.9f

            val emberColor = when {
                alpha > 0.6f -> Color(0xFFFFD700)
                alpha > 0.3f -> Color(0xFFFF4500)
                else -> Color(0xFFFF8C00)
            }
            drawCircle(
                color = emberColor.copy(alpha = alpha),
                radius = r,
                center = Offset(xOffset.toFloat(), yOffset)
            )
        }
    }
}

// ---------------------------------------------------------
// 9. Samurai — Katana, mon (family crest), bamboo
// ---------------------------------------------------------
@Composable
fun SamuraiBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "samurai")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(78000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(400)
        val cx = size.width / 2f
        val cy = size.height / 2f

        val monRadius = size.width * 0.12f
        drawCircle(
            color = Color(0xFFC0A080).copy(alpha = 0.08f),
            radius = monRadius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = Color(0xFFC0A080).copy(alpha = 0.06f),
            radius = monRadius * 0.8f,
            style = Stroke(width = 1.5f),
            center = Offset(cx, cy)
        )
        for (i in 0..7) {
            val angle = (i * PI.toFloat()) / 4f
            val innerR = monRadius * 0.3f
            val outerR = monRadius * 0.9f
            drawLine(
                color = Color(0xFFC0A080).copy(alpha = 0.06f),
                start = Offset(cx + cos(angle) * innerR, cy + sin(angle) * innerR),
                end = Offset(cx + cos(angle) * outerR, cy + sin(angle) * outerR),
                strokeWidth = 1f
            )
        }

        val xBase = size.width * 0.15f
        for (b in 0..4) {
            val bx = xBase + b * 18f
            val by = size.height * 0.2f + sin(time * 0.1f + b * 0.5f) * 5f
            drawLine(
                color = Color(0xFF4A7C59).copy(alpha = 0.2f),
                start = Offset(bx, by),
                end = Offset(bx, size.height * 0.9f),
                strokeWidth = 3f
            )
            for (k in 0..5) {
                val knotY = by + (size.height * 0.7f) * (k / 5f)
                val knotX = bx + 10f + sin(knotY * 0.1f) * 6f
                drawLine(
                    color = Color(0xFF4A7C59).copy(alpha = 0.15f),
                    start = Offset(bx, knotY),
                    end = Offset(knotX, knotY - 10f),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = Color(0xFF4A7C59).copy(alpha = 0.15f),
                    start = Offset(bx, knotY),
                    end = Offset(knotX, knotY + 10f),
                    strokeWidth = 1.5f
                )
            }
        }

        for (i in 0..12) {
            val speedY = 15f + random.nextFloat() * 25f
            val speedX = 25f + random.nextFloat() * 35f
            val yOffset = (time * speedY + random.nextFloat() * 3000f) % (size.height + 100f) - 50f
            val xOffset = (time * speedX + random.nextFloat() * 2000f) % (size.width + 100f) - 50f

            withTransform({
                translate(xOffset, yOffset)
                rotate(time * 6f + random.nextFloat() * 360f)
            }) {
                val leafPath = Path().apply {
                    moveTo(0f, -12f)
                    quadraticBezierTo(8f, 0f, 0f, 12f)
                    quadraticBezierTo(-8f, 0f, 0f, -12f)
                }
                drawPath(leafPath, color = Color(0xFF8B0000).copy(alpha = 0.25f))
            }
        }

        val katanaX = size.width * 0.75f
        val katanaY = size.height * 0.3f
        val bladeLength = size.height * 0.35f
        val bladeAngle = sin(time * 0.05f) * 0.1f

        withTransform({
            rotate(bladeAngle * 180f / PI.toFloat(), pivot = Offset(katanaX, katanaY))
        }) {
            val bladePath = Path().apply {
                moveTo(katanaX - 3f, katanaY)
                lineTo(katanaX - 1f, katanaY + bladeLength)
                lineTo(katanaX + 1f, katanaY + bladeLength)
                lineTo(katanaX + 3f, katanaY)
                close()
            }
            drawPath(bladePath, color = Color(0xFFE0E0E0).copy(alpha = 0.12f))

            drawLine(
                color = Color(0xFFE0E0E0).copy(alpha = 0.15f),
                start = Offset(katanaX, katanaY),
                end = Offset(katanaX, katanaY + bladeLength),
                strokeWidth = 1f
            )

            val tsubaR = 8f
            drawCircle(
                color = Color(0xFFC0A080).copy(alpha = 0.15f),
                radius = tsubaR,
                center = Offset(katanaX, katanaY + bladeLength * 0.15f)
            )
        }
    }
}

// ---------------------------------------------------------
// 10. Aurora — Curtain-like aurora borealis
// ---------------------------------------------------------
@Composable
fun AuroraBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(120000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val bands = listOf(
            color1 to 0f,
            color2 to 0.3f,
            color3 to 0.6f,
            color1 to 0.9f
        )

        bands.forEach { (color, phaseOffset) ->
            val path = Path()
            val baseY = size.height * 0.15f
            val amp = 40f + sin(time * 0.5f + phaseOffset * 3f) * 20f

            path.moveTo(0f, size.height)
            for (i in 0..size.width.toInt() step 6) {
                val x = i.toFloat()
                val y = baseY + sin((x / 120f) + time + phaseOffset * 2f) * amp +
                        sin((x / 60f) + time * 1.5f + phaseOffset) * amp * 0.5f
                path.lineTo(x, y)
            }
            path.lineTo(size.width, size.height)
            path.close()

            drawPath(
                path,
                color = color.copy(alpha = 0.08f + sin(time * 0.3f + phaseOffset) * 0.04f)
            )
        }

        val pulseAlpha = (sin(time * 0.7f) + 1f) / 2f * 0.12f + 0.04f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color1.copy(alpha = pulseAlpha),
                    color2.copy(alpha = pulseAlpha * 0.5f),
                    Color.Transparent
                )
            ),
            size = size
        )
    }
}

// ---------------------------------------------------------
// 11. Nocturno — Refined night sky with more stars
// ---------------------------------------------------------
@Composable
fun NightBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "night")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(56000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(500)

        for (i in 0..60) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height * 0.6f
            val r = random.nextFloat() * 2f + 0.3f
            val twinkle = (sin(time * 0.02f + random.nextFloat() * 10f) + 1f) / 2f
            val alpha = twinkle * 0.3f + 0.2f
            drawCircle(Color.White.copy(alpha = alpha), radius = r, center = Offset(x, y))
        }

        val shootPhase = time % 1000f
        if (shootPhase < 150f) {
            val startX = size.width * 0.8f - shootPhase * 4f
            val startY = size.height * 0.1f + shootPhase * 3f
            val progress = shootPhase / 150f
            drawLine(
                color = Color.White.copy(alpha = 1f - progress),
                start = Offset(startX, startY),
                end = Offset(startX - 50f, startY + 50f),
                strokeWidth = 2f - progress
            )
        }
    }
}

// ---------------------------------------------------------
// 12. RETRO PIXEL — Experiencia 8-bit definitiva
// ---------------------------------------------------------
private data class RetroCloudDef(
    val startY: Float, val size: Int, val speed: Float, val alpha: Float
)

private val retroStarSeeds = listOf(
    floatArrayOf(0.08f, 0.12f), floatArrayOf(0.22f, 0.05f), floatArrayOf(0.35f, 0.18f),
    floatArrayOf(0.45f, 0.08f), floatArrayOf(0.55f, 0.15f), floatArrayOf(0.68f, 0.06f),
    floatArrayOf(0.78f, 0.20f), floatArrayOf(0.88f, 0.10f), floatArrayOf(0.15f, 0.25f),
    floatArrayOf(0.50f, 0.22f), floatArrayOf(0.72f, 0.28f), floatArrayOf(0.92f, 0.03f),
    floatArrayOf(0.05f, 0.30f), floatArrayOf(0.40f, 0.32f), floatArrayOf(0.62f, 0.12f),
    floatArrayOf(0.82f, 0.35f), floatArrayOf(0.30f, 0.38f), floatArrayOf(0.95f, 0.40f),
    floatArrayOf(0.10f, 0.42f), floatArrayOf(0.58f, 0.36f), floatArrayOf(0.75f, 0.08f),
    floatArrayOf(0.48f, 0.28f), floatArrayOf(0.02f, 0.08f), floatArrayOf(0.98f, 0.15f),
    floatArrayOf(0.20f, 0.35f), floatArrayOf(0.65f, 0.30f), floatArrayOf(0.38f, 0.14f),
    floatArrayOf(0.85f, 0.24f), floatArrayOf(0.12f, 0.18f), floatArrayOf(0.52f, 0.34f)
)

private val retroMountData = listOf(
    0.08f to 0.35f, 0.20f to 0.50f, 0.32f to 0.30f,
    0.45f to 0.55f, 0.55f to 0.25f, 0.68f to 0.45f,
    0.78f to 0.38f, 0.92f to 0.52f
)

private val retroTreePositions = listOf(0.08f, 0.18f, 0.28f, 0.40f, 0.52f, 0.62f, 0.75f, 0.88f, 0.95f)

private val retroClouds = listOf(
    RetroCloudDef(0.08f, 4, 12f, 0.15f),
    RetroCloudDef(0.15f, 3, 18f, 0.12f),
    RetroCloudDef(0.05f, 5, 8f, 0.18f),
    RetroCloudDef(0.20f, 3, 22f, 0.10f),
    RetroCloudDef(0.12f, 4, 14f, 0.14f),
    RetroCloudDef(0.18f, 2, 28f, 0.08f)
)

private val retroMoonPattern = listOf(
    "   1111   ",
    "  111111  ",
    " 11111111 ",
    "1111111111",
    "1111111111",
    "1111111111",
    "1111111111",
    " 11111111 ",
    "  111111  ",
    "   1111   "
)

@Composable
fun RetroPixelBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "retro")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(180000, easing = LinearEasing)),
        label = "time"
    )

    val cloudTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(120000, easing = LinearEasing)),
        label = "clouds"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val pw = 8f
        val groundY = size.height * 0.82f

        // ── 1. SKY GRADIENT (pixelated bands) ──
        val skyBands = 32
        val bandHeight = groundY / skyBands
        for (band in 0 until skyBands) {
            val t = band.toFloat() / skyBands
            drawRect(
                color = Color(
                    (11 + t * 22).toInt().coerceIn(0, 255),
                    (11 + t * 38).toInt().coerceIn(0, 255),
                    (59 + t * 55).toInt().coerceIn(0, 255)
                ),
                topLeft = Offset(0f, band * bandHeight),
                size = Size(size.width, bandHeight + 1f)
            )
        }

        // ── 2. STARS ──
        for (seed in retroStarSeeds) {
            val sx = seed[0] * size.width
            val sy = seed[1] * groundY * 0.7f
            val twinkle = (sin(time * 0.03f + sx * 0.1f + sy * 0.1f) + 1f) / 2f
            val alpha = 0.15f + twinkle * 0.6f
            val starSize = if (seed[1] > 0.2f) 2f else 3f
            drawRect(
                color = Color.White.copy(alpha = alpha.coerceIn(0f, 1f)),
                topLeft = Offset(sx, sy),
                size = Size(starSize, starSize)
            )
            if (starSize > 2f && twinkle > 0.7f) {
                drawRect(
                    color = Color.White.copy(alpha = (twinkle - 0.7f) * 0.5f),
                    topLeft = Offset(sx - 2f, sy), size = Size(7f, 1f)
                )
                drawRect(
                    color = Color.White.copy(alpha = (twinkle - 0.7f) * 0.5f),
                    topLeft = Offset(sx, sy - 2f), size = Size(1f, 7f)
                )
            }
        }

        // ── 3. PIXEL MOON ──
        val moonX = size.width * 0.78f
        val moonY = groundY * 0.12f
        val moonR = size.width * 0.045f
        for (my in retroMoonPattern.indices) {
            for (mx in retroMoonPattern[my].indices) {
                if (retroMoonPattern[my][mx] == '1') {
                    drawRect(
                        color = Color(0xFFFFFDE7),
                        topLeft = Offset(
                            moonX - moonR + mx * (moonR * 2f / retroMoonPattern[0].length),
                            moonY - moonR + my * (moonR * 2f / retroMoonPattern.size)
                        ),
                        size = Size(moonR * 0.22f, moonR * 0.22f)
                    )
                }
            }
        }
        drawRect(
            color = Color(0xFFFFFDE7).copy(alpha = 0.06f),
            topLeft = Offset(moonX - moonR * 1.5f, moonY - moonR * 1.5f),
            size = Size(moonR * 3f, moonR * 3f)
        )

        // ── 4. MOUNTAINS ──
        val mountPath1 = Path().apply {
            moveTo(0f, groundY)
            for (seg in 0..size.width.toInt() step 4) {
                val x = seg.toFloat()
                var y = groundY
                for ((mx, my) in retroMountData) {
                    val cx = mx * size.width
                    val cy = my * groundY * 0.35f
                    val dist = abs(x - cx)
                    if (dist < size.width * 0.25f) {
                        y = minOf(y, groundY * 0.65f - cy * (1f - dist / (size.width * 0.25f)))
                    }
                }
                lineTo(x, y)
            }
            lineTo(size.width, groundY); close()
        }
        drawPath(mountPath1, color = Color(0xFF1A1A3E))

        val mountPath2 = Path().apply {
            moveTo(0f, groundY)
            for (seg in 0..size.width.toInt() step 4) {
                val x = seg.toFloat()
                var y = groundY
                for ((i, pair) in retroMountData.withIndex()) {
                    val cx = (i.toFloat() / retroMountData.size) * size.width +
                            sin(time * 0.005f + i) * 10f
                    val cy = pair.second * groundY * 0.25f
                    val dist = abs(x - cx)
                    if (dist < size.width * 0.20f) {
                        y = minOf(y, groundY * 0.75f - cy * (1f - dist / (size.width * 0.20f)))
                    }
                }
                lineTo(x, y.coerceAtMost(groundY * 0.75f))
            }
            lineTo(size.width, groundY); close()
        }
        drawPath(mountPath2, color = Color(0xFF12122E))

        // ── 5. PIXEL TREES ──
        for (pos in retroTreePositions) {
            val tx = pos * size.width
            val treeH = 30f + (pos * 40f) % 35f
            drawRect(
                color = Color(0xFF5D4037),
                topLeft = Offset(tx - 3f, groundY - treeH),
                size = Size(6f, treeH)
            )
            for (ly in 0..4) {
                for (lx in 0..6) {
                    val shade = when {
                        (ly == 1 && lx in 1..5) || (ly == 2 && lx in 0..6) ||
                                (ly == 3 && lx in 1..5) -> 1
                        (ly == 0 && lx in 2..4) || (ly == 1 && lx == 3) ||
                                (ly == 2 && lx in 2..4) -> 2
                        else -> 0
                    }
                    if (shade > 0) {
                        drawRect(
                            color = if (shade == 2) Color(0xFF1B5E20) else Color(0xFF2E7D32),
                            topLeft = Offset(tx - 14f + lx * 5f, groundY - treeH - 20f + ly * 5f),
                            size = Size(5f, 5f)
                        )
                    }
                }
            }
        }

        // ── 6. GROUND ──
        val grassRand = java.util.Random(123)
        for (gx in 0..(size.width / pw).toInt()) {
            val shade = grassRand.nextInt(3)
            drawRect(
                color = when (shade) {
                    0 -> Color(0xFF1B5E20)
                    1 -> Color(0xFF2E7D32)
                    else -> Color(0xFF388E3C)
                }.copy(alpha = 0.7f + grassRand.nextFloat() * 0.3f),
                topLeft = Offset(gx * pw, groundY),
                size = Size(pw, size.height - groundY)
            )
        }

        // ── 7. GRASS TEXTURE ──
        for (gx in 0..(size.width / 4f).toInt()) {
            val grassH = 2f + (sin(gx * 1.7f + time * 0.01f) + 1f) * 3f
            drawRect(
                color = listOf(Color(0xFF4CAF50), Color(0xFF66BB6A), Color(0xFF81C784))
                    .random(java.util.Random(gx.hashCode())).copy(alpha = 0.5f),
                topLeft = Offset(gx * 4f, groundY - grassH),
                size = Size(2f, grassH)
            )
        }

        // ── 8. PIXEL CLOUDS ──
        for (cloud in retroClouds) {
            val cw = cloud.size * 8f
            val ch = cloud.size * 4f
            val cx = (cloudTime * cloud.speed + cloud.startY * 2000f) %
                    (size.width + cw * 2f) - cw
            val cy = cloud.startY * groundY * 0.5f
            val cols = 7
            val rows = 5
            for (iy in 0 until rows) {
                for (ix in 0 until cols) {
                    val isFilled = when {
                        iy == 0 -> ix in 2..4
                        iy == 1 -> ix in 1..5
                        iy == 2 -> ix in 0..6
                        iy == 3 -> ix in 1..5
                        else -> ix in 2..4
                    }
                    if (isFilled) {
                        drawRect(
                            color = Color.White.copy(alpha = cloud.alpha),
                            topLeft = Offset(cx + ix * (cw / cols), cy + iy * (ch / rows)),
                            size = Size(cw / cols, ch / rows)
                        )
                    }
                }
            }
        }

        // ── 9. FIREFLIES ──
        val fireflyRandom = java.util.Random(456)
        for (i in 0..8) {
            val fx = fireflyRandom.nextFloat() * size.width
            val fy = groundY * 0.4f + (time * (5f + i * 2f) + fireflyRandom.nextFloat() * 2000f) %
                    (groundY * 0.5f)
            val flicker = (sin(time * 0.1f + i * 2.5f) + 1f) / 2f
            val a = (flicker * 0.4f + 0.1f).coerceIn(0f, 1f)
            drawRect(color = Color(0xFFFFE600).copy(alpha = a), topLeft = Offset(fx, fy), size = Size(3f, 3f))
            drawRect(color = Color(0xFFFFE600).copy(alpha = a * 0.3f), topLeft = Offset(fx - 2f, fy - 2f), size = Size(7f, 7f))
        }

        // ── 10. GRID OVERLAY ──
        val gridSpacing = 16f
        for (gx in 0..(size.width / gridSpacing).toInt()) {
            drawRect(color = Color.White.copy(alpha = 0.015f), topLeft = Offset(gx * gridSpacing, 0f), size = Size(1f, size.height))
        }
        for (gy in 0..(size.height / gridSpacing).toInt()) {
            drawRect(color = Color.White.copy(alpha = 0.015f), topLeft = Offset(0f, gy * gridSpacing), size = Size(size.width, 1f))
        }

        // ── 11. SCANLINES ──
        for (scanY in 0..(size.height / 4f).toInt()) {
            drawRect(color = Color.Black.copy(alpha = 0.04f), topLeft = Offset(0f, scanY * 4f), size = Size(size.width, 1f))
        }
    }
}
