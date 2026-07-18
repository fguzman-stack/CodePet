package com.tamagotchi.code.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Componente que renderiza la mascota Codey con micro-animaciones nativas de Compose.
 *
 * @param status El estado actual de la mascota (ej. "HAPPY", "SICK", "SLEEPING").
 * @param celebrationTrigger Flujo que dispara la animación de celebración.
 * @param onClick Acción al pulsar sobre la mascota.
 * @param modifier Modificador para el contenedor.
 */
@Composable
fun AnimatedPetSprite(
    status: String,
    level: Int = 1,
    equippedHat: String? = null,
    celebrationTrigger: SharedFlow<Unit>,
    learningEventTrigger: SharedFlow<String>? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reduceMotion = LocalReduceMotion.current
    
    val evolutionStage = remember(level) { PetEvolutionStage.fromLevel(level) }

    // --- ESTADOS DE ANIMACIÓN ---
    
    // Animación de celebración (Bounce + Wobble)
    val bounceAnim = remember { Animatable(0f) }
    val wobbleAnim = remember { Animatable(0f) }
    val flashAnim = remember { Animatable(1f) }

    // Suscripción al trigger de aprendizaje
    LaunchedEffect(learningEventTrigger) {
        learningEventTrigger?.collect { type ->
            if (!reduceMotion) {
                when (type) {
                    "SUCCESS" -> {
                        launch {
                            flashAnim.animateTo(1.5f, tween(100))
                            flashAnim.animateTo(1f, tween(200))
                        }
                    }
                    "FAILURE" -> {
                        launch {
                            wobbleAnim.animateTo(-10f, tween(50))
                            wobbleAnim.animateTo(10f, tween(50))
                            wobbleAnim.animateTo(0f, tween(50))
                        }
                    }
                }
            }
        }
    }
    
    // Suscripción al trigger de celebración
    LaunchedEffect(celebrationTrigger) {
        celebrationTrigger.collect {
            if (!reduceMotion) {
                launch {
                    bounceAnim.animateTo(
                        targetValue = -20f,
                        animationSpec = tween(150, easing = FastOutSlowInEasing)
                    )
                    bounceAnim.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                launch {
                    wobbleAnim.animateTo(4f, tween(100))
                    wobbleAnim.animateTo(-4f, tween(200))
                    wobbleAnim.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                }
            }
        }
    }

    // --- ANIMACIONES INFINITAS (LOOPS) ---
    val infiniteTransition = rememberInfiniteTransition(label = "PetLoop")

    // Idle Breathing (Respiración)
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (status != "SLEEPING" && !reduceMotion) 1.03f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Breathing"
    )

    // Sick Tremble (Temblor por enfermedad)
    val trembleOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Tremble"
    )

    // Hungry Pulse (Pulso por hambre)
    val hungryScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HungryPulse"
    )

    // Sleeping Opacity (Fade de respiración dormido)
    val sleepAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SleepAlpha"
    )

    // --- LÓGICA DE PARPADEO (BLINK) ---
    var isBlinking by remember { mutableStateOf(false) }
    
    // El parpadeo solo aplica en estados donde tiene sentido y no hay reducción de movimiento
    val canBlink = remember(status, reduceMotion) {
        !reduceMotion && (status == "HAPPY" || status == "EXCITED" || status == "STUDYING")
    }

    LaunchedEffect(canBlink) {
        if (canBlink) {
            while (true) {
                delay(Random.nextLong(3000, 5000))
                isBlinking = true
                delay(180)
                isBlinking = false
            }
        }
    }

    // --- RESOLUCIÓN DE RECURSOS ---
    val context = LocalContext.current
    
    val baseResId = remember(status) { getPetDrawable(status) }
    val blinkResId = remember(status) { getPetBlinkDrawable(status) }
    
    val interactionSource = remember { MutableInteractionSource() }
    
    // Verificamos si el asset de blink existe realmente
    val hasBlinkAsset = remember(blinkResId) {
        try {
            context.resources.getResourceName(blinkResId)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Idle Sway (Pequeño balanceo lateral aleatorio para dar más vida)
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Sway"
    )

    // --- COMPOSICIÓN FINAL ---
    Box(
        modifier = modifier
            .offset(
                x = when {
                    status == "SICK" && !reduceMotion -> trembleOffset.dp
                    !reduceMotion -> swayOffset.dp
                    else -> 0.dp
                },
                y = bounceAnim.value.dp
            )
            .scale(
                when {
                    status == "HUNGRY" && !reduceMotion -> hungryScale
                    status != "SLEEPING" && !reduceMotion -> breathingScale * flashAnim.value
                    else -> 1.0f * flashAnim.value
                } * when(evolutionStage) {
                    PetEvolutionStage.Egg -> 0.8f
                    PetEvolutionStage.Child -> 0.9f
                    PetEvolutionStage.Adult -> 1.0f
                    PetEvolutionStage.Veteran -> 1.1f
                    PetEvolutionStage.Legendary -> 1.2f
                }
            )
            .graphicsLayer {
                rotationZ = wobbleAnim.value
                alpha = if (status == "SLEEPING" && !reduceMotion) sleepAlpha else 1.0f
            },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isBlinking && hasBlinkAsset,
            animationSpec = tween(100),
            label = "BlinkCrossfade"
        ) { blink ->
            val resToDraw = if (blink) blinkResId else baseResId
            Image(
                painter = painterResource(id = resToDraw),
                contentDescription = "Codey Status: $status",
                modifier = Modifier
                    .size(170.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onClick() },
                contentScale = ContentScale.Fit
            )
        }

        // Overlay de Sombrero (Idea #7)
        equippedHat?.let { hatId ->
            val hatIcon = getHatIcon(hatId)
            if (hatIcon != null) {
                androidx.compose.material3.Icon(
                    imageVector = hatIcon,
                    contentDescription = "Hat: $hatId",
                    tint = getHatColor(hatId),
                    modifier = Modifier
                        .size(60.dp)
                        .offset(y = (-65).dp)
                        .scale(if (evolutionStage == PetEvolutionStage.Egg) 0.7f else 1f)
                )
            }
        }
    }
}

/**
 * Mapea el ID de sombrero a su Icono Material.
 */
private fun getHatIcon(hatId: String): androidx.compose.ui.graphics.vector.ImageVector? = when (hatId) {
    "dev_cap" -> Icons.Default.DeveloperMode
    "grad_cap" -> Icons.Default.School
    "vr_helmet" -> Icons.Default.Headset
    "chef_hat" -> Icons.Default.Restaurant
    "crown" -> Icons.Default.Star
    else -> null
}

private fun getHatColor(hatId: String): androidx.compose.ui.graphics.Color = when (hatId) {
    "crown" -> androidx.compose.ui.graphics.Color(0xFFFFD700) // Gold
    "vr_helmet" -> androidx.compose.ui.graphics.Color(0xFF29B6F6) // Light Blue
    "grad_cap" -> androidx.compose.ui.graphics.Color(0xFF424242) // Dark Gray
    "chef_hat" -> androidx.compose.ui.graphics.Color(0xFFE0E0E0) // Light Gray
    else -> androidx.compose.ui.graphics.Color(0xFF78909C) // Blue Gray
}

sealed class PetEvolutionStage {
    object Egg : PetEvolutionStage()
    object Child : PetEvolutionStage()
    object Adult : PetEvolutionStage()
    object Veteran : PetEvolutionStage()
    object Legendary : PetEvolutionStage()

    companion object {
        fun fromLevel(level: Int): PetEvolutionStage = when {
            level < 5 -> Egg
            level < 10 -> Child
            level < 25 -> Adult
            level < 50 -> Veteran
            else -> Legendary
        }
    }
}

/**
 * Mapea el estado a su drawable base.
 */
private fun getPetDrawable(status: String): Int = when (status) {
    "SLEEPING" -> R.drawable.mascota_sleeping
    "STUDYING" -> R.drawable.mascota_studying
    "SICK" -> R.drawable.mascota_sick
    "SAD" -> R.drawable.mascota_sad
    "HUNGRY" -> R.drawable.mascota_hungry
    "EXCITED" -> R.drawable.mascota_excited
    else -> R.drawable.mascota_happy
}

/**
 * Mapea el estado a su drawable de parpadeo (Blink).
 * TODO: Generar estos assets y añadirlos a res/drawable/
 */
private fun getPetBlinkDrawable(status: String): Int = when (status) {
    "HAPPY" -> try { R.drawable::class.java.getField("mascota_happy_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_happy }
    "EXCITED" -> try { R.drawable::class.java.getField("mascota_excited_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_excited }
    "STUDYING" -> try { R.drawable::class.java.getField("mascota_studying_blink").getInt(null) } catch(e: Exception) { R.drawable.mascota_studying }
    else -> getPetDrawable(status)
}
