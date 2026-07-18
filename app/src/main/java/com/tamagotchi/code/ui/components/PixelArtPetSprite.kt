package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun PixelArtPetSprite(
    status: String,
    level: Int = 1,
    equippedSkin: String,
    celebrationTrigger: SharedFlow<Unit>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var bounce by remember { mutableStateOf(0f) }
    
    val animatedBounce by animateFloatAsState(
        targetValue = bounce,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "PixelBounce"
    )

    LaunchedEffect(celebrationTrigger) {
        celebrationTrigger.collect {
            bounce = -20f
            delay(150)
            bounce = 0f
        }
    }

    var frame by remember { mutableIntStateOf(0) }
    LaunchedEffect(status) {
        while (true) {
            val delayMs = if (status == "SLEEPING") 1000L else if (status == "EXCITED") 300L else 600L
            delay(delayMs)
            frame = (frame + 1) % 2 // 2 frames animation
        }
    }

    // Definición de las skins en Pixel Art (Matriz 12x12)
    // 0 = vacío, 1 = primario, 2 = secundario (ojos), 3 = acento
    val matrices = when (equippedSkin) {
        "skin_robot" -> {
            if (frame == 0) {
                listOf(
                    "000111111000",
                    "001111111100",
                    "011221122110",
                    "011221122110",
                    "011111111110",
                    "011333333110",
                    "011311113110",
                    "001111111100",
                    "000110011000",
                    "000110011000",
                    "000110011000",
                    "001110011100"
                )
            } else {
                listOf(
                    "000000000000",
                    "000111111000",
                    "001111111100",
                    "011221122110",
                    "011221122110",
                    "011111111110",
                    "011333333110",
                    "011311113110",
                    "001111111100",
                    "000110011000",
                    "000110011000",
                    "001110011100"
                )
            }
        }
        else -> { // skin_alien
            if (frame == 0) {
                listOf(
                    "000000000000",
                    "000100001000",
                    "001111111100",
                    "011111111110",
                    "011221122110",
                    "111111111111",
                    "111111111111",
                    "111113311111",
                    "011111111110",
                    "001111111100",
                    "000110011000",
                    "001110011100"
                )
            } else {
                listOf(
                    "000100001000",
                    "001100001100",
                    "001111111100",
                    "011111111110",
                    "011221122110",
                    "111111111111",
                    "111111111111",
                    "111113311111",
                    "011111111110",
                    "001111111100",
                    "000110011000",
                    "001110011100"
                )
            }
        }
    }

    val primaryColor = if (equippedSkin == "skin_robot") Color(0xFF9E9E9E) else Color(0xFF81C784)
    val eyeColor = if (status == "SLEEPING" || status == "SICK") Color(0xFF616161) else Color(0xFF212121)
    val accentColor = if (equippedSkin == "skin_robot") Color(0xFFFFB300) else Color(0xFF388E3C)

    val scaleModifier = when (PetEvolutionStage.fromLevel(level)) {
        PetEvolutionStage.Egg -> 0.8f
        PetEvolutionStage.Child -> 0.9f
        PetEvolutionStage.Adult -> 1.0f
        PetEvolutionStage.Veteran -> 1.1f
        PetEvolutionStage.Legendary -> 1.2f
    }

    Box(
        modifier = modifier
            .offset(y = animatedBounce.dp)
            .scale(scaleModifier)
            .graphicsLayer {
                if (status == "SICK") rotationZ = (kotlin.random.Random.nextFloat() - 0.5f) * 4f
            },
        contentAlignment = Alignment.Center
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Canvas(
            modifier = Modifier
                .size(170.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onClick() }
        ) {
            val blockSize = size.width / 12f

            matrices.forEachIndexed { y, row ->
                row.forEachIndexed { x, char ->
                    val color = when (char) {
                        '1' -> primaryColor
                        '2' -> eyeColor
                        '3' -> accentColor
                        else -> Color.Transparent
                    }
                    if (color != Color.Transparent) {
                        // Ojos cerrados si duerme
                        if (char == '2' && status == "SLEEPING" && y % 2 == 0) return@forEachIndexed
                        
                        drawRect(
                            color = color,
                            topLeft = Offset(x * blockSize, y * blockSize),
                            size = Size(blockSize, blockSize)
                        )
                    }
                }
            }
        }
    }
}
