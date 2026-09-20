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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import android.graphics.Paint
import androidx.compose.animation.core.*
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
                "Default" -> DefaultBackground(theme.primary, theme.secondary, theme.accent, theme.isDark)
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
// 0. Default — soft drifting light orbs (works for light & dark)
// ---------------------------------------------------------
@Composable
fun DefaultBackground(primary: Color, secondary: Color, accent: Color, isDark: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "default")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(64000, easing = LinearEasing)),
        label = "drift"
    )
    val introAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(2500, easing = LinearOutSlowInEasing),
        label = "default_intro"
    )
    val orbColors = listOf(primary, secondary, accent)
    val maxAlpha = if (isDark) 0.16f else 0.11f
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        orbColors.forEachIndexed { i, color ->
            val phase = i * (2f * PI.toFloat() / 3f)
            val cx = w * (0.28f + 0.44f * i / 2f) + sin(time * 0.6f + phase) * w * 0.16f
            val cy = h * (0.24f + 0.2f * i) + cos(time * 0.45f + phase) * h * 0.12f
            val r = w * (0.42f + sin(time * 0.3f + phase) * 0.05f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = maxAlpha * introAlpha), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = r
                ),
                radius = r,
                center = Offset(cx, cy)
            )
        }
        val dust = java.util.Random(77)
        for (i in 0..18) {
            val baseX = dust.nextFloat() * w
            val baseY = dust.nextFloat() * h
            val speed = 0.2f + dust.nextFloat() * 0.5f
            val drift = ((time * speed * 6f + dust.nextFloat() * 100f) % (h + 40f)) - 20f
            val alpha = (0.10f + sin(time * 2f + i) * 0.05f) * introAlpha
            drawCircle(
                color = (if (isDark) Color.White else primary).copy(alpha = alpha.coerceIn(0f, 1f)),
                radius = 1.6f,
                center = Offset(baseX + sin(time + i) * 14f, baseY - drift)
            )
        }
    }
}

// ---------------------------------------------------------
// 1. Matrix Green — 1/0 rain with hidden pet messages & speed bursts
// ---------------------------------------------------------
@Composable
fun MatrixBackground(primary: Color, accent: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "matrix")

    val baseTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(320000, easing = LinearEasing)),
        label = "baseTime"
    )

    // Animación de aparición progresiva (intro fade)
    val introAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(3000, easing = LinearOutSlowInEasing),
        label = "matrix_intro"
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

    // Usamos un pool de caracteres fijo para evitar Random excesivo en el loop de dibujo
    val characters = remember { charArrayOf('0', '1') }
    
    // Optimizamos usando NativeCanvas para evitar el overhead de TextMeasurer en loops grandes
    val paint = remember {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            isAntiAlias = false // Matrix es pixelado, desactivar AA ahorra CPU
            isFakeBoldText = true
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val numColumns = (size.width / 32f).toInt()
        val random = java.util.Random(42)
        val speedMultiplier = 1f + burstFactor * 0.5f
        
        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            
            for (i in 0 until numColumns) {
                val speed = (8f + random.nextFloat() * 12f) * speedMultiplier
                val yOffset = (baseTime * speed + random.nextFloat() * 2000f) % (size.height + 200f) - 100f
                val length = 6 + random.nextInt(6)
                val columnSize = 24f + random.nextFloat() * 24f // Tamaño variable por columna para efecto de profundidad

                // Staggered column appearance based on horizontal position
                val columnIntroFactor = (introAlpha * 1.5f - (i.toFloat() / numColumns)).coerceIn(0f, 1f)
                if (columnIntroFactor <= 0f) continue

                for (j in 0 until length) {
                    val alpha = (1f - (j.toFloat() / length)) * columnIntroFactor
                    val char = characters[random.nextInt(characters.size)]
                    
                    val y = yOffset - j * 32f
                    if (y in -40f..size.height + 40f) {
                        paint.color = (if (j == 0) primary else primary.copy(alpha = 0.4f))
                            .copy(alpha = alpha.coerceIn(0f, 1f))
                            .toArgb()
                        paint.textSize = columnSize
                        
                        nativeCanvas.drawText(
                            char.toString(),
                            i * 32f + 16f,
                            y,
                            paint
                        )
                    }
                }
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
// 3. Cyberpunk — synthwave sunset, perspective grid, skyline & rain
// ---------------------------------------------------------
@Composable
fun CyberpunkBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyberpunk")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(48000, easing = LinearEasing)),
        label = "time"
    )
    val glitchPhase by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(24000, easing = LinearEasing)),
        label = "glitch"
    )

    val skyline = remember {
        java.util.Random(7).let { r -> List(26) { Triple(r.nextFloat(), 0.25f + r.nextFloat() * 0.75f, r.nextFloat()) } }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val horizon = h * 0.58f
        val sunX = w / 2f

        // ── neon sky glow over horizon ──
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = 0.10f), color2.copy(alpha = 0.06f)),
                startY = horizon - h * 0.3f,
                endY = horizon
            ),
            topLeft = Offset(0f, horizon - h * 0.3f),
            size = Size(w, h * 0.3f)
        )

        // ── striped synthwave sun ──
        val sunR = w * 0.20f * (1f + sin(time * 0.21f) * 0.012f)
        drawCircle(
            brush = Brush.verticalGradient(
                listOf(color3.copy(alpha = 0.65f), color1.copy(alpha = 0.5f)),
                startY = horizon - sunR, endY = horizon + sunR
            ),
            radius = sunR,
            center = Offset(sunX, horizon)
        )
        var slice = 0
        while (slice < 9) {
            val gapY = horizon + (slice + 1.6f) * sunR * 0.12f
            val gapH = sunR * 0.02f + slice * sunR * 0.011f
            if (gapY < horizon + sunR) {
                val halfW = kotlin.math.sqrt((sunR * sunR - (gapY - horizon) * (gapY - horizon)).coerceAtLeast(0f))
                drawRect(
                    color = Color(0xFF0B0514).copy(alpha = 0.92f),
                    topLeft = Offset(sunX - halfW, gapY),
                    size = Size(halfW * 2f, gapH)
                )
            }
            slice++
        }

        // ── building silhouettes on the horizon ──
        skyline.forEach { (fx, heightF, lit) ->
            val bw = w * 0.035f
            val bh = h * 0.16f * heightF
            val bx = fx * w
            drawRect(color = Color(0xFF16091F).copy(alpha = 0.92f), topLeft = Offset(bx, horizon - bh), size = Size(bw, bh))
            if (lit > 0.45f) {
                val rows = (bh / (10f + bw)).toInt().coerceAtMost(6)
                for (r in 0 until rows) {
                    val flickerOn = sin(time * 0.9f + fx * 40f + r * 2.7f) > -0.55f
                    if (flickerOn && (r + (fx * 100).toInt()) % 2 == 0) {
                        drawRect(
                            color = (if (lit > 0.75f) color2 else color3).copy(alpha = 0.5f),
                            topLeft = Offset(bx + bw * 0.25f, horizon - bh + r * (bh / (rows + 1)) + bh / (rows + 1) * 0.5f),
                            size = Size(bw * 0.2f, 2f)
                        )
                    }
                }
            }
        }

        // ── perspective grid toward the viewer ──
        val vanishX = sunX
        for (i in -14..14) {
            drawLine(
                color = color2.copy(alpha = 0.22f),
                start = Offset(vanishX + i * w * 0.02f, horizon),
                end = Offset(vanishX + i * w * 0.42f, h),
                strokeWidth = 1.5f
            )
        }
        val scroll = (time * 0.08f) % 1f
        var k = 0f
        while (k < 12f) {
            val p = (k + scroll) / 12f
            val y = horizon + (h - horizon) * (p * p)
            if (y > horizon) {
                drawLine(
                    color = color1.copy(alpha = 0.10f + 0.28f * p),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f + p * 2f
                )
            }
            k += 1f
        }
        drawLine(color = color1.copy(alpha = 0.6f), start = Offset(0f, horizon), end = Offset(w, horizon), strokeWidth = 2f)

        // ── neon rain streaks ──
        val rain = java.util.Random(31)
        for (i in 0 until 24) {
            val speed = 240f + rain.nextFloat() * 260f
            val rx = rain.nextFloat() * w
            val len = 26f + rain.nextFloat() * 34f
            val ry = ((time * speed + rain.nextFloat() * 2400f) % (h + 120f)) - 60f
            drawLine(
                color = (if (i % 3 == 0) color2 else color1).copy(alpha = 0.12f + (i % 5) * 0.02f),
                start = Offset(rx + ry * 0.06f, ry),
                end = Offset(rx + (ry + len) * 0.06f, ry + len),
                strokeWidth = 1.2f
            )
        }

        // ── chromatic glitch bars ──
        val glitchCount = (sin(glitchPhase * PI.toFloat() * 4f) * 3f + 4f).toInt().coerceAtLeast(0)
        val glitchRandom = java.util.Random(99)
        for (g in 0 until glitchCount) {
            val gy = glitchRandom.nextFloat() * h
            val gx = glitchRandom.nextFloat() * w * 0.5f
            val gw = 30f + glitchRandom.nextFloat() * 120f
            drawRect(color = color1.copy(alpha = 0.16f), topLeft = Offset(gx, gy), size = Size(gw, 2f))
            drawRect(color = color2.copy(alpha = 0.16f), topLeft = Offset(gx + 5f, gy + 1.5f), size = Size(gw * 0.7f, 2f))
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
// 6. Neón — neon horizon grid, expanding bloom rings & flicker
// ---------------------------------------------------------
@Composable
fun NeonBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(40000, easing = LinearEasing)),
        label = "time"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val horizon = h * 0.68f

        // ── horizon bloom ──
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = 0.12f), Color.Transparent),
                startY = horizon - h * 0.06f, endY = horizon + h * 0.06f
            ),
            topLeft = Offset(0f, horizon - h * 0.06f),
            size = Size(w, h * 0.12f)
        )

        // ── perspective floor grid ──
        for (i in -10..10) {
            drawLine(
                color = color2.copy(alpha = 0.16f),
                start = Offset(w / 2f + i * w * 0.02f, horizon),
                end = Offset(w / 2f + i * w * 0.5f, h),
                strokeWidth = 1f + (abs(i) % 3) * 0.5f
            )
        }
        val scroll = (time * 0.12f) % 1f
        var row = 0f
        while (row < 10f) {
            val p = (row + scroll) / 10f
            val y = horizon + (h - horizon) * (p * p)
            drawLine(
                color = color1.copy(alpha = 0.08f + 0.22f * p),
                start = Offset(0f, y), end = Offset(w, y),
                strokeWidth = 0.8f + p * 1.8f
            )
            row += 1f
        }
        drawLine(color = color2.copy(alpha = 0.7f), start = Offset(0f, horizon), end = Offset(w, horizon), strokeWidth = 2f)

        // ── expanding bloom rings (3 passes per ring = fake glow) ──
        val cx = w / 2f
        val cy = horizon
        for (i in 0 until 4) {
            val p = ((time * 0.15f + i * 2.5f) % 10f) / 10f
            if (p > 0.02f) {
                val r = p * w * 0.85f
                val fade = (1f - p) * 0.5f
                val ringColor = listOf(color1, color2, color3)[i % 3]
                drawCircle(ringColor.copy(alpha = fade * 0.12f), radius = r, center = Offset(cx, cy), style = Stroke(width = 10f))
                drawCircle(ringColor.copy(alpha = fade * 0.3f), radius = r, center = Offset(cx, cy), style = Stroke(width = 4f))
                drawCircle(ringColor.copy(alpha = fade * 0.9f), radius = r, center = Offset(cx, cy), style = Stroke(width = 1.4f))
            }
        }

        // ── floating orbs with glow + neon-sign flicker ──
        val orbRandom = java.util.Random(1234)
        for (i in 0 until 5) {
            val ox = orbRandom.nextFloat() * w
            val oy = orbRandom.nextFloat() * horizon * 0.8f
            val orbColor = listOf(color1, color2, color3)[i % 3]
            val flicker = if (i == 2 && sin(time * 9f) > 0.86f) 0.25f else 1f
            val pulse = (0.55f + sin(time * 0.9f + i * 1.7f) * 0.35f) * flicker
            val r = 5f + i * 2.2f
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(orbColor.copy(alpha = 0.5f * pulse), Color.Transparent),
                    center = Offset(ox, oy + sin(time * 0.5f + i) * 10f),
                    radius = r * 5f
                ),
                radius = r * 5f,
                center = Offset(ox, oy + sin(time * 0.5f + i) * 10f)
            )
            drawCircle(orbColor.copy(alpha = (0.9f * pulse).coerceIn(0f, 1f)), radius = r * 0.4f,
                center = Offset(ox, oy + sin(time * 0.5f + i) * 10f))
        }
    }
}

// ---------------------------------------------------------
// 7. Océano — god rays, layered waves, fish, seaweed & bubbles
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
        val w = size.width
        val h = size.height

        // ── god rays from the surface ──
        for (r in 0 until 4) {
            val baseX = w * (0.15f + r * 0.24f)
            val sway = sin(time * 0.4f + r * 1.8f) * w * 0.05f
            val topW = 26f + r * 8f
            val botW = topW * 3.4f
            val alpha = 0.045f + sin(time * 0.7f + r * 2.2f) * 0.02f
            val ray = Path().apply {
                moveTo(baseX + sway - topW / 2f, 0f)
                lineTo(baseX + sway + topW / 2f, 0f)
                lineTo(baseX + sway + topW / 2f + botW, h * 0.86f)
                lineTo(baseX + sway - botW / 2f, h * 0.86f)
                close()
            }
            drawPath(ray, color = Color(0xFFBFEFFF).copy(alpha = alpha.coerceIn(0f, 1f)))
        }

        // ── layered waves with crest highlights ──
        val waveColors = listOf(
            Color(0xFF00B4D8),
            Color(0xFF48CAE4),
            Color(0xFF90E0EF),
            Color(0xFF03045E)
        )
        for (wv in waveColors.indices) {
            val path = Path()
            val baseY = h * (0.5f + wv * 0.12f)
            val waveHeight = 30f + wv * 12f
            val freq = 80f + wv * 30f
            val phase = wv * 1.2f
            path.moveTo(0f, h)
            val crest = ArrayList<Float>((w / 8f).toInt() + 2)
            for (i in 0..w.toInt() step 8) {
                val x = i.toFloat()
                val y = baseY + sin((x / freq) + time * 0.8f + phase) * waveHeight
                crest.add(y)
                path.lineTo(x, y)
            }
            path.lineTo(w, h)
            path.close()
            drawPath(path, color = waveColors[wv].copy(alpha = 0.22f - wv * 0.035f))
            var ci = 1
            while (ci < crest.size) {
                val crestShine = (sin(ci * 0.9f + time * 1.6f + wv) + 1f) / 2f
                if (crestShine > 0.72f) {
                    drawLine(
                        color = Color.White.copy(alpha = (crestShine - 0.72f) * 0.6f),
                        start = Offset((ci - 1) * 8f, crest[ci - 1] - 1f),
                        end = Offset(ci * 8f, crest[ci] - 1f),
                        strokeWidth = 1.6f
                    )
                }
                ci++
            }
        }

        // ── seaweed ribbons swaying on the seabed ──
        val weedRandom = java.util.Random(88)
        for (s in 0 until 7) {
            val sx = w * (0.06f + s * 0.14f) + weedRandom.nextFloat() * 30f
            val heightFrac = 0.10f + weedRandom.nextFloat() * 0.12f
            val weedPath = Path()
            weedPath.moveTo(sx, h)
            val segs = 8
            for (seg in 1..segs) {
                val frac = seg / segs.toFloat()
                val yy = h - h * heightFrac * frac
                val xx = sx + sin(time * 1.1f + frac * 3f + s * 1.4f) * 14f * frac
                weedPath.quadraticBezierTo(sx + (xx - sx) * 0.4f, (yy + h * heightFrac * (frac - 0.06f)), xx, yy)
            }
            drawPath(weedPath, color = Color(0xFF1B5E20).copy(alpha = 0.35f), style = Stroke(width = 5f))
            drawPath(weedPath, color = Color(0xFF2E7D32).copy(alpha = 0.22f), style = Stroke(width = 2f))
        }

        // ── fish silhouettes crossing at depth ──
        for (f in 0 until 6) {
            val depth = 0.60f + (f % 3) * 0.12f
            val dir = if (f % 2 == 0) 1f else -1f
            val speed = 0.05f + (f % 4) * 0.018f
            val fx = ((time * speed * dir * 60f + f * 173f) % (w + 160f) + w + 160f) % (w + 160f) - 80f
            val fy = h * depth + sin(time * 2f + f * 2.1f) * 8f
            val fs = (4f + (f % 3) * 2.5f) * dir
            val tail = sin(time * 9f + f) * fs * 0.18f
            drawOval(
                color = Color(0xFF90CAF9).copy(alpha = 0.10f + (f % 3) * 0.03f),
                topLeft = Offset(fx - abs(fs), fy - abs(fs) * 0.45f),
                size = Size(abs(fs) * 2f, abs(fs) * 0.9f)
            )
            drawPath(
                Path().apply {
                    moveTo(fx + fs, fy)
                    lineTo(fx + fs * 1.7f, fy - abs(fs) * 0.5f + tail)
                    lineTo(fx + fs * 1.7f, fy + abs(fs) * 0.5f + tail)
                    close()
                },
                color = Color(0xFF90CAF9).copy(alpha = 0.10f + (f % 3) * 0.03f)
            )
        }

        // ── rising bubbles with wobble and highlight ──
        val bubbleRandom = java.util.Random(500)
        for (i in 0..14) {
            val seedX = bubbleRandom.nextFloat() * w
            val speed = 12f + bubbleRandom.nextFloat() * 20f
            val by = h + 30f - ((time * speed + bubbleRandom.nextFloat() * 900f) % (h + 60f))
            val br = 2f + bubbleRandom.nextFloat() * 4.5f
            val bx = seedX + sin(time * 2f + i * 1.3f) * 10f
            drawCircle(
                color = Color.White.copy(alpha = 0.16f),
                radius = br, center = Offset(bx, by), style = Stroke(width = 1f)
            )
            drawCircle(color = Color.White.copy(alpha = 0.28f), radius = br * 0.28f,
                center = Offset(bx - br * 0.35f, by - br * 0.35f))
        }

        // ── drifting plankton specks ──
        val plankton = java.util.Random(404)
        for (i in 0 until 30) {
            val px = (plankton.nextFloat() * w + time * (4f + plankton.nextFloat() * 6f)) % w
            val py = (plankton.nextFloat() * h + sin(time + i) * 6f)
            drawCircle(Color(0xFFB2EBF2).copy(alpha = 0.08f + (i % 4) * 0.02f), radius = 1f, center = Offset(px, py))
        }
    }
}

// ---------------------------------------------------------
// 8. Volcánico — flowing lava with crust cracks, bubbles & heat haze
// ---------------------------------------------------------
@Composable
fun VolcanicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "volcano")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(90000, easing = LinearEasing)),
        label = "time"
    )
    val cracks = remember {
        java.util.Random(301).let { r ->
            List(9) {
                val points = ArrayList<Float>()
                var x = r.nextFloat()
                val drift = (r.nextFloat() - 0.5f) * 0.08f
                repeat(7) { points.add(x); x += (r.nextFloat() - 0.5f) * 0.05f + drift }
                Triple(r.nextFloat(), r.nextFloat() * 2f * PI.toFloat(), points)
            }
        }
    }

    fun lavaSurface(x: Float, w: Float, t: Float, baseY: Float, shift: Float = 0f): Float =
        baseY + sin((x / (w * 0.14f)) + t * 0.9f + shift) * 14f + sin((x / (w * 0.09f)) + t * 0.55f + shift) * 8f

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val lavaBaseY = h * 0.86f

        // ── pulsing under-glow ──
        val glowPulse = 0.16f + sin(time * 0.5f) * 0.06f
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color.Transparent, Color(0xFFFF4500).copy(alpha = glowPulse)),
                startY = h * 0.62f, endY = h
            ),
            topLeft = Offset(0f, h * 0.62f), size = Size(w, h * 0.38f)
        )

        // ── lava body with flowing gradient ──
        val flow = (time * 0.25f) % 2f
        val lavaPath = Path().apply {
            moveTo(0f, h)
            for (i in 0..w.toInt() step 6) lineTo(i.toFloat(), lavaSurface(i.toFloat(), w, time, lavaBaseY))
            lineTo(w, h); close()
        }
        drawPath(
            lavaPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB21E00).copy(alpha = 0.55f),
                    Color(0xFFFF4500).copy(alpha = 0.55f),
                    Color(0xFFFF8C00).copy(alpha = 0.45f),
                    Color(0xFFFF4500).copy(alpha = 0.55f)
                ),
                start = Offset(w * (flow - 1f), lavaBaseY), end = Offset(w * (flow + 1f), h)
            )
        )

        // ── cooler crust band behind a second, slower flow ──
        val crustPath = Path().apply {
            moveTo(0f, h)
            for (i in 0..w.toInt() step 6) lineTo(i.toFloat(), lavaSurface(i.toFloat(), w, time * 0.6f, lavaBaseY + 26f, 2f))
            lineTo(w, h); close()
        }
        drawPath(crustPath, color = Color(0xFF2A0800).copy(alpha = 0.55f))
        drawPath(
            Path().apply {
                moveTo(0f, lavaSurface(0f, w, time * 0.6f, lavaBaseY + 26f, 2f))
                for (i in 6..w.toInt() step 6) lineTo(i.toFloat(), lavaSurface(i.toFloat(), w, time * 0.6f, lavaBaseY + 26f, 2f))
            },
            color = Color(0xFFFF6D00).copy(alpha = 0.28f + sin(time * 0.8f) * 0.08f),
            style = Stroke(width = 2.5f)
        )

        // ── glowing crust cracks across the lava ──
        cracks.forEach { (xF, phase, pts) ->
            val pulse = 0.25f + (sin(time * 1.1f + phase) + 1f) / 2f * 0.55f
            val crackPath = Path()
            pts.forEachIndexed { idx, fx ->
                val x = fx * w
                val y = lavaSurface(x, w, time, lavaBaseY) + idx * ((h - lavaBaseY) / pts.size)
                if (idx == 0) crackPath.moveTo(x, y) else crackPath.lineTo(x, y)
            }
            drawPath(crackPath, color = Color(0xFFFF8C00).copy(alpha = pulse * 0.28f), style = Stroke(width = 5f))
            drawPath(crackPath, color = Color(0xFFFFD700).copy(alpha = pulse * 0.8f), style = Stroke(width = 1.2f))
        }

        // ── bubbles rising through the surface film and popping ──
        val bub = java.util.Random(302)
        for (i in 0 until 10) {
            val bx = bub.nextFloat()
            val speed = 0.25f + bub.nextFloat() * 0.3f
            val p = ((time * speed + i * 0.37f) % 1.4f)
            if (p < 1f) {
                val x = bx * w
                val surfaceY = lavaSurface(x, w, time, lavaBaseY)
                val r = 3f + p * 6f
                val alpha = (1f - p) * 0.7f
                if (p < 0.8f) {
                    drawCircle(Color(0xFFFFB74D).copy(alpha = alpha), radius = r, center = Offset(x, surfaceY - r * 0.4f))
                    drawCircle(Color(0xFFFFE082).copy(alpha = alpha * 0.6f), radius = r * 0.35f, center = Offset(x - r * 0.25f, surfaceY - r * 0.6f))
                } else {
                    drawCircle(
                        color = Color(0xFFFFB74D).copy(alpha = (1f - p) * 0.9f),
                        radius = r * 1.8f,
                        center = Offset(x, surfaceY - r * 0.4f),
                        style = Stroke(width = 1.2f)
                    )
                }
            }
        }

        // ── heat haze wisps floating up from the lava ──
        val haze = java.util.Random(303)
        for (i in 0 until 6) {
            val hx = haze.nextFloat() * w
            val rise = (time * (0.12f + haze.nextFloat() * 0.1f) + i * 0.4f) % 1f
            val hy = lavaBaseY - rise * h * 0.22f
            val wa = (1f - rise) * 0.05f
            val wiggle = sin(rise * 12f + i * 2f) * 10f
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFFF8C00).copy(alpha = wa), Color.Transparent),
                    startY = hy - 40f, endY = hy
                ),
                topLeft = Offset(hx + wiggle - 22f, hy - 40f),
                size = Size(44f, 40f)
            )
        }

        // ── embers with flicker ──
        val random = java.util.Random(300)
        for (i in 0..50) {
            val speed = 15f + random.nextFloat() * 50f
            val seed = random.nextFloat()
            val yOffset = h + 50f - ((time * speed + seed * 2000f) % (h + 100f))
            val xOffset = random.nextFloat() * w + sin(time * 0.3f + seed * PI.toFloat()) * 40f
            val r = random.nextFloat() * 5f + 1.5f
            val climb = ((h - yOffset) / h).coerceIn(0f, 1f)
            val flicker = 0.8f + sin(time * (4f + seed * 6f) + i) * 0.2f
            val alpha = (climb * 0.9f * flicker).coerceIn(0f, 1f)
            val emberColor = when {
                alpha > 0.6f -> Color(0xFFFFD700)
                alpha > 0.3f -> Color(0xFFFF4500)
                else -> Color(0xFFFF8C00)
            }
            drawCircle(color = emberColor.copy(alpha = alpha), radius = r, center = Offset(xOffset.toFloat(), yOffset))
        }
    }
}

// ---------------------------------------------------------
// 9. Samurai — red sun, mist, bamboo groves, maple leaves & katana glint
// ---------------------------------------------------------
@Composable
fun SamuraiBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "samurai")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(78000, easing = LinearEasing)),
        label = "time"
    )

    val bambooSeeds = remember {
        java.util.Random(401).let { r -> List(8) { Triple(r.nextFloat(), 0.06f + r.nextFloat() * 0.88f, r.nextFloat()) } }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(400)
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // ── pale red sun with soft breathing edge ──
        val sunX = w * 0.24f
        val sunY = h * 0.18f
        val sunR = w * 0.16f * (1f + sin(time * 0.11f) * 0.02f)
        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color(0xFFB03A2E).copy(alpha = 0.22f), Color(0xFFB03A2E).copy(alpha = 0.07f), Color.Transparent),
                center = Offset(sunX, sunY), radius = sunR * 1.6f
            ),
            radius = sunR * 1.6f, center = Offset(sunX, sunY)
        )
        drawCircle(color = Color(0xFFC0392B).copy(alpha = 0.16f), radius = sunR, center = Offset(sunX, sunY))

        // ── mist bands drifting at different depths ──
        for (m in 0 until 3) {
            val my = h * (0.30f + m * 0.22f)
            val drift = ((time * (6f + m * 4f)) % (w + 400f)) - 200f
            drawRect(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, Color(0xFFD8CFC8).copy(alpha = 0.045f + m * 0.01f), Color.Transparent),
                    startX = drift - 200f, endX = drift + 200f
                ),
                topLeft = Offset(0f, my), size = Size(w, h * 0.06f)
            )
        }

        // ── mon (family crest) watermark ──
        val monRadius = w * 0.12f
        drawCircle(color = Color(0xFFC0A080).copy(alpha = 0.08f), radius = monRadius, center = Offset(cx, cy))
        drawCircle(
            color = Color(0xFFC0A080).copy(alpha = 0.06f), radius = monRadius * 0.8f,
            style = Stroke(width = 1.5f), center = Offset(cx, cy)
        )
        for (i in 0..7) {
            val angle = (i * PI.toFloat()) / 4f + time * 0.01f
            drawLine(
                color = Color(0xFFC0A080).copy(alpha = 0.06f),
                start = Offset(cx + cos(angle) * monRadius * 0.3f, cy + sin(angle) * monRadius * 0.3f),
                end = Offset(cx + cos(angle) * monRadius * 0.9f, cy + sin(angle) * monRadius * 0.9f),
                strokeWidth = 1f
            )
        }

        // ── bamboo groves flanking both edges, swaying ──
        bambooSeeds.forEachIndexed { b, (xF, topF, seed) ->
            val bx = xF * w
            val by = h * topF
            if (bx < w * 0.14f || bx > w * 0.86f) {
                val sway = sin(time * 0.12f + b * 0.9f) * 5f
                val stalk = Path().apply {
                    moveTo(bx, h)
                    quadraticBezierTo(bx + sway * 0.3f, (by + h) / 2f, bx + sway, by)
                }
                drawPath(stalk, color = Color(0xFF4A7C59).copy(alpha = 0.20f), style = Stroke(width = 3.5f))
                for (k in 0..5) {
                    val frac = (k + 1) / 7f
                    val knotY = h - (h - by) * frac
                    val knotX = bx + sway * frac * frac
                    drawLine(
                        color = Color(0xFF4A7C59).copy(alpha = 0.16f),
                        start = Offset(knotX - 6f, knotY), end = Offset(knotX + seed * 12f + 6f, knotY - 8f - seed * 8f),
                        strokeWidth = 1.5f
                    )
                    drawLine(
                        color = Color(0xFF4A7C59).copy(alpha = 0.16f),
                        start = Offset(knotX, knotY), end = Offset(knotX - 8f - seed * 10f, knotY - 10f - seed * 6f),
                        strokeWidth = 1.5f
                    )
                }
            }
        }

        // ── falling maple leaves tumbling across ──
        val leafColors = listOf(
            Color(0xFF8B0000), Color(0xFFB03A2E), Color(0xFFC9622B), Color(0xFFD98E32)
        )
        for (i in 0 until 14) {
            val speedY = 14f + random.nextFloat() * 22f
            val speedX = 18f + random.nextFloat() * 30f
            val yOffset = (time * speedY + random.nextFloat() * 3000f) % (h + 100f) - 50f
            val xOffset = ((time * speedX + random.nextFloat() * 2000f + sin(time * 0.8f + i) * 40f) % (w + 100f)) - 50f
            val sizeF = 0.7f + random.nextFloat() * 0.8f
            withTransform({
                translate(xOffset, yOffset)
                rotate(time * (4f + (i % 5) * 2f) + i * 47f)
                scale(sizeF, sizeF, pivot = Offset.Zero)
            }) {
                val leafPath = Path().apply {
                    moveTo(0f, -12f)
                    quadraticBezierTo(9f, -6f, 6f, 2f)
                    quadraticBezierTo(9f, 8f, 0f, 12f)
                    quadraticBezierTo(-9f, 8f, -6f, 2f)
                    quadraticBezierTo(-9f, -6f, 0f, -12f)
                    close()
                }
                drawPath(leafPath, color = leafColors[i % leafColors.size].copy(alpha = 0.22f))
                drawLine(
                    Color(0xFF3E1F0D).copy(alpha = 0.18f),
                    start = Offset(0f, -9f), end = Offset(0f, 11f), strokeWidth = 0.8f
                )
            }
        }

        // ── katana with periodic glint sweep ──
        val katanaX = w * 0.76f
        val katanaY = h * 0.28f
        val bladeLength = h * 0.34f
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
            drawPath(bladePath, color = Color(0xFFE0E0E0).copy(alpha = 0.14f))
            val glint = (time * 0.35f) % 3f
            if (glint < 1f) {
                val gy = katanaY + (bladeLength * glint)
                val fade = sin(glint * PI.toFloat())
                drawLine(
                    color = Color.White.copy(alpha = 0.5f * fade),
                    start = Offset(katanaX - 2f, gy), end = Offset(katanaX + 2f, gy + 24f),
                    strokeWidth = 1.5f
                )
            }
            drawCircle(
                color = Color(0xFFC0A080).copy(alpha = 0.16f),
                radius = 8f, center = Offset(katanaX, katanaY + bladeLength * 0.15f)
            )
        }
    }
}

// ---------------------------------------------------------
// 10. Aurora — shimmering curtains, stars & mirrored glow
// ---------------------------------------------------------
@Composable
fun AuroraBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(120000, easing = LinearEasing)),
        label = "time"
    )
    val stars = remember {
        java.util.Random(66).let { r -> List(46) { floatArrayOf(r.nextFloat(), r.nextFloat() * 0.55f, r.nextFloat() * 10f) } }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // ── twinkling stars behind the curtains ──
        stars.forEachIndexed { i, s ->
            val twinkle = (sin(time * 2.2f + s[2]) + 1f) / 2f
            drawCircle(
                color = Color.White.copy(alpha = 0.12f + twinkle * 0.45f),
                radius = if (s[2] > 8f) 1.6f else 0.9f,
                center = Offset(s[0] * w, s[1] * h)
            )
            if (twinkle > 0.93f && i % 4 == 0) {
                drawLine(Color.White.copy(alpha = 0.4f), Offset(s[0] * w - 4f, s[1] * h), Offset(s[0] * w + 4f, s[1] * h), 0.8f)
                drawLine(Color.White.copy(alpha = 0.4f), Offset(s[0] * w, s[1] * h - 4f), Offset(s[0] * w, s[1] * h + 4f), 0.8f)
            }
        }

        // ── aurora curtains drawn as shimmering vertical strips ──
        val bands = listOf(
            Triple(color1, 0.10f, 40f),
            Triple(color2, 0.24f, 60f),
            Triple(color3, 0.05f, 50f),
            Triple(color1, 0.30f, 30f)
        )
        val strip = 12f
        bands.forEach { (color, phaseOffset, heightF) ->
            val baseY = h * (0.12f + phaseOffset * 0.18f)
            var x = 0f
            while (x < w) {
                val crest = baseY +
                        sin((x / (110f + phaseOffset * 200f)) + time * (1f + phaseOffset) + phaseOffset * 3f) * 42f +
                        sin((x / 55f) + time * 1.5f) * 16f
                val curtainH = h * heightF * (0.55f + 0.45f * sin(time + x * 0.01f + phaseOffset * 6f))
                val shimmer = 0.5f + 0.5f * sin(x * 0.11f + time * 3f + phaseOffset * 9f)
                drawLine(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = (0.16f + shimmer * 0.14f)),
                            color.copy(alpha = 0.05f + shimmer * 0.03f),
                            Color.Transparent
                        ),
                        startY = crest,
                        endY = crest + curtainH.toFloat().coerceAtMost(h - crest)
                    ),
                    start = Offset(x, crest),
                    end = Offset(x, crest + curtainH),
                    strokeWidth = strip + 2f
                )
                x += strip
            }
        }

        // ── mirrored glow near the horizon (faint reflection) ──
        val pulseAlpha = (sin(time * 0.7f) + 1f) / 2f * 0.08f + 0.03f
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = pulseAlpha), color2.copy(alpha = pulseAlpha * 0.6f)),
                startY = h * 0.72f, endY = h
            ),
            topLeft = Offset(0f, h * 0.72f),
            size = Size(w, h * 0.28f)
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
                    .random(kotlin.random.Random(gx.hashCode().toLong())).copy(alpha = 0.5f),
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
