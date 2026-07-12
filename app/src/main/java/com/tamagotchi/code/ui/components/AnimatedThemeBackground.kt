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
import com.tamagotchi.code.ui.theme.AppTheme
import kotlin.math.sin
import kotlin.math.cos
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
                "Matrix Green" -> MatrixBackground(theme.primary)
                "Galáctico" -> GalacticBackground()
                "Cyberpunk" -> CyberpunkBackground(theme.primary, theme.secondary)
                "Sakura" -> SakuraBackground()
                "Minimalista" -> MinimalistBackground()
                "Neón" -> NeonBackground(theme.primary, theme.secondary)
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
// 1. Matrix Green (Digital Rain simplified to blocks)
// ---------------------------------------------------------
@Composable
fun MatrixBackground(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "matrix")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing)),
        label = "time"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val numColumns = (size.width / 40f).toInt()
        val random = java.util.Random(42) // Fixed seed for stable columns
        for (i in 0 until numColumns) {
            val speed = 50f + random.nextFloat() * 100f
            val yOffset = (time * speed + random.nextFloat() * 1000f) % (size.height + 200f) - 100f
            val length = 4 + random.nextInt(6)
            for (j in 0 until length) {
                val alpha = 1f - (j.toFloat() / length)
                drawRect(
                    color = color.copy(alpha = alpha * 0.8f),
                    topLeft = Offset(i * 40f + 10f, yOffset - j * 30f),
                    size = Size(15f, 20f)
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 2. Galáctico (Stars + Parallax)
// ---------------------------------------------------------
@Composable
fun GalacticBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)),
        label = "twinkle"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(100)
        for (i in 0..60) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val r = random.nextFloat() * 3f + 1f
            val phase = random.nextFloat() * 2f * Math.PI.toFloat()
            val alpha = (sin(time + phase) + 1f) / 2f * 0.8f + 0.2f
            drawCircle(Color.White.copy(alpha = alpha), radius = r, center = Offset(x, y))
        }
    }
}

// ---------------------------------------------------------
// 3. Cyberpunk (Moving Grid)
// ---------------------------------------------------------
@Composable
fun CyberpunkBackground(color1: Color, color2: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyberpunk")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "grid"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val spacing = 100f
        // Vertical lines
        for (i in 0..(size.width / spacing).toInt() + 1) {
            drawLine(
                color = color2.copy(alpha = 0.2f),
                start = Offset(i * spacing, 0f),
                end = Offset(i * spacing, size.height),
                strokeWidth = 2f
            )
        }
        // Horizontal lines moving down
        var y = offset
        while (y < size.height) {
            drawLine(
                color = color1.copy(alpha = 0.3f),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
            y += spacing
        }
    }
}

// ---------------------------------------------------------
// 4. Sakura (Falling Petals)
// ---------------------------------------------------------
@Composable
fun SakuraBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "sakura")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "fall"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(200)
        for (i in 0..30) {
            val speedY = 30f + random.nextFloat() * 50f
            val speedX = 10f + random.nextFloat() * 20f
            val phaseX = random.nextFloat() * 2 * PI
            
            val yOffset = (time * speedY + random.nextFloat() * 2000f) % (size.height + 100f) - 50f
            val xOffset = (random.nextFloat() * size.width) + sin(time * 0.1f + phaseX) * speedX * 2f
            
            withTransform({
                translate(xOffset.toFloat(), yOffset)
                rotate((time * (10f + random.nextFloat() * 20f) + random.nextFloat() * 360f) % 360f)
            }) {
                drawOval(
                    color = Color(0xFFFFB7C5).copy(alpha = 0.6f),
                    topLeft = Offset(-8f, -12f),
                    size = Size(16f, 24f)
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 5. Minimalista (Slow Large Circles)
// ---------------------------------------------------------
@Composable
fun MinimalistBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "minimal")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing)),
        label = "orbit"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Black.copy(alpha = 0.02f),
            radius = size.width * 0.6f,
            center = Offset(
                size.width / 2 + sin(time) * 100f,
                size.height / 3 + cos(time) * 100f
            )
        )
        drawCircle(
            color = Color.Black.copy(alpha = 0.015f),
            radius = size.width * 0.5f,
            center = Offset(
                size.width / 2 + cos(time + 1f) * 150f,
                size.height / 1.5f + sin(time + 1f) * 150f
            )
        )
    }
}

// ---------------------------------------------------------
// 6. Neón (Pulsing Glow)
// ---------------------------------------------------------
@Composable
fun NeonBackground(color1: Color, color2: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, color1.copy(alpha = 0.1f * pulse), color2.copy(alpha = 0.2f * pulse)),
                center = Offset(size.width / 2, size.height / 2),
                radius = size.width
            ),
            size = size
        )
    }
}

// ---------------------------------------------------------
// 7. Océano (Sine Waves)
// ---------------------------------------------------------
@Composable
fun OceanBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "ocean")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "wave"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val path1 = Path()
        val path2 = Path()
        val path3 = Path()
        
        val waveHeight = 40f
        
        path1.moveTo(0f, size.height * 0.8f)
        path2.moveTo(0f, size.height * 0.85f)
        path3.moveTo(0f, size.height * 0.9f)
        
        for (i in 0..size.width.toInt() step 10) {
            val x = i.toFloat()
            path1.lineTo(x, size.height * 0.8f + sin((x / 100f) + time) * waveHeight)
            path2.lineTo(x, size.height * 0.85f + sin((x / 120f) + time + 1f) * waveHeight * 1.2f)
            path3.lineTo(x, size.height * 0.9f + sin((x / 80f) + time + 2f) * waveHeight * 0.8f)
        }
        
        path1.lineTo(size.width, size.height)
        path1.lineTo(0f, size.height)
        path2.lineTo(size.width, size.height)
        path2.lineTo(0f, size.height)
        path3.lineTo(size.width, size.height)
        path3.lineTo(0f, size.height)
        
        drawPath(path1, color = Color(0xFF00B4D8).copy(alpha = 0.2f))
        drawPath(path2, color = Color(0xFF48CAE4).copy(alpha = 0.3f))
        drawPath(path3, color = Color(0xFF90E0EF).copy(alpha = 0.4f))
    }
}

// ---------------------------------------------------------
// 8. Volcánico (Embers)
// ---------------------------------------------------------
@Composable
fun VolcanicBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "volcano")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)),
        label = "embers"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(300)
        for (i in 0..40) {
            val speed = 20f + random.nextFloat() * 60f
            val yOffset = size.height + 100f - ((time * speed + random.nextFloat() * 2000f) % (size.height + 200f))
            val xOffset = random.nextFloat() * size.width + sin(time * 0.2f + random.nextFloat() * PI) * 30f
            val r = random.nextFloat() * 4f + 2f
            val alpha = (yOffset / size.height).coerceIn(0f, 1f) * 0.8f // fade out at top
            
            drawCircle(
                color = Color(0xFFFF6D00).copy(alpha = alpha),
                radius = r,
                center = Offset(xOffset.toFloat(), yOffset)
            )
        }
    }
}

// ---------------------------------------------------------
// 9. Samurai (Bamboo/Leaves)
// ---------------------------------------------------------
@Composable
fun SamuraiBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "samurai")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing)),
        label = "leaves"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(400)
        for (i in 0..20) {
            val speedY = 20f + random.nextFloat() * 30f
            val speedX = 30f + random.nextFloat() * 40f
            
            val yOffset = (time * speedY + random.nextFloat() * 2000f) % (size.height + 100f) - 50f
            val xOffset = (time * speedX + random.nextFloat() * 2000f) % (size.width + 100f) - 50f
            
            withTransform({
                translate(xOffset, yOffset)
                rotate(time * 10f + random.nextFloat() * 360f)
            }) {
                val leafPath = Path().apply {
                    moveTo(0f, -15f)
                    quadraticBezierTo(10f, 0f, 0f, 15f)
                    quadraticBezierTo(-10f, 0f, 0f, -15f)
                }
                drawPath(leafPath, color = Color(0xFF8B0000).copy(alpha = 0.3f))
            }
        }
    }
}

// ---------------------------------------------------------
// 10. Aurora (Soft color blends)
// ---------------------------------------------------------
@Composable
fun AuroraBackground(color1: Color, color2: Color, color3: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "blend"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(listOf(color1.copy(alpha = 0.2f), Color.Transparent)),
            radius = size.width,
            center = Offset(sin(time) * 200f, cos(time) * 300f)
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(color2.copy(alpha = 0.2f), Color.Transparent)),
            radius = size.width * 1.2f,
            center = Offset(size.width + cos(time) * 200f, size.height / 2 + sin(time) * 200f)
        )
        drawCircle(
            brush = Brush.radialGradient(listOf(color3.copy(alpha = 0.2f), Color.Transparent)),
            radius = size.width * 0.8f,
            center = Offset(size.width / 2 + sin(time * 1.5f) * 150f, size.height + cos(time * 1.5f) * 150f)
        )
    }
}

// ---------------------------------------------------------
// 11. Nocturno (Shooting Stars)
// ---------------------------------------------------------
@Composable
fun NightBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "night")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "stars"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(500)
        // Static stars
        for (i in 0..40) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * (size.height / 2) // Mostly in top half
            val r = random.nextFloat() * 1.5f + 0.5f
            drawCircle(Color.White.copy(alpha = random.nextFloat() * 0.5f + 0.2f), radius = r, center = Offset(x, y))
        }
        
        // Shooting star
        val shootPhase = time % 1000f
        if (shootPhase < 200f) {
            val startX = size.width * 0.8f
            val startY = size.height * 0.1f
            val length = shootPhase * 3f
            drawLine(
                color = Color.White.copy(alpha = 1f - (shootPhase / 200f)),
                start = Offset(startX - length, startY + length),
                end = Offset(startX - length - 40f, startY + length + 40f),
                strokeWidth = 2f
            )
        }
    }
}

// ---------------------------------------------------------
// 12. Retro Pixel (Blocky Clouds)
// ---------------------------------------------------------
@Composable
fun RetroPixelBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "retro")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(25000, easing = LinearEasing)),
        label = "clouds"
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = java.util.Random(600)
        val pixelSize = 16f
        
        for (i in 0..4) {
            val y = 100f + i * 150f
            val speed = 20f + random.nextFloat() * 30f
            val xOffset = (time * speed + random.nextFloat() * 2000f) % (size.width + 300f) - 300f
            
            // Draw a blocky cloud
            val cloudColor = Color.White.copy(alpha = 0.15f)
            drawRect(cloudColor, Offset(xOffset, y), Size(pixelSize * 4, pixelSize))
            drawRect(cloudColor, Offset(xOffset - pixelSize, y + pixelSize), Size(pixelSize * 6, pixelSize))
            drawRect(cloudColor, Offset(xOffset - pixelSize * 2, y + pixelSize * 2), Size(pixelSize * 8, pixelSize))
            drawRect(cloudColor, Offset(xOffset - pixelSize, y + pixelSize * 3), Size(pixelSize * 5, pixelSize))
        }
    }
}
