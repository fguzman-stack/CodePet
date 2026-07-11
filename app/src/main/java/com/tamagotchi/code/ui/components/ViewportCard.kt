package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ViewportCard(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onRenameClick: () -> Unit,
    onPlayClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val bounceScale = remember { Animatable(1f) }
    val bounceOffsetY = remember { Animatable(0f) }
    val heartOffsetY = remember { Animatable(0f) }
    val heartAlpha = remember { Animatable(0f) }
    var showHeart by remember { mutableStateOf(false) }

    val appTheme = LocalAppTheme.current
    val cardShape = RoundedCornerShape(appTheme.cornerRadius)

    fun onPetTap() {
        viewModel.petThePet()
        viewModel.soundManager.playClick()
        scope.launch {
            showHeart = true
            bounceScale.snapTo(1f)
            bounceOffsetY.snapTo(0f)
            heartOffsetY.snapTo(0f)
            heartAlpha.snapTo(1f)
            launch { bounceScale.animateTo(1.25f, tween(100)); bounceScale.animateTo(1f, spring(dampingRatio = 0.3f)) }
            launch { bounceOffsetY.animateTo(-20f, tween(100)); bounceOffsetY.animateTo(0f, spring(dampingRatio = 0.3f)) }
            launch { heartOffsetY.animateTo(-120f, tween(800)); heartAlpha.animateTo(0f, tween(800)) }
            delay(900)
            showHeart = false
        }
    }

    val petImageRes = when (state.currentStatus) {
        "SLEEPING" -> R.drawable.mascota_sleeping
        "STUDYING" -> R.drawable.mascota_studying
        "SICK" -> R.drawable.mascota_sick
        "SAD" -> R.drawable.mascota_sad
        "HUNGRY" -> R.drawable.mascota_hungry
        "EXCITED" -> R.drawable.mascota_excited
        else -> R.drawable.mascota_happy
    }

    val statusColor = when (state.currentStatus) {
        "SLEEPING" -> Color(0xFF64B5F6)
        "STUDYING" -> MaterialTheme.colorScheme.primary
        "SICK" -> MaterialTheme.colorScheme.error
        "SAD" -> Color(0xFF90A4AE)
        "HUNGRY" -> Color(0xFFFFB74D)
        "EXCITED" -> Color(0xFFFF80AB)
        else -> MaterialTheme.colorScheme.secondary
    }

    val randomQuote = remember(state.currentStatus) {
        val quotes = when (state.currentStatus) {
            "SLEEPING" -> listOf(
                "Zzz... if (dream) { sleep() } else { repeat() }... Zzz",
                "Cargando baterías... no interrumpas mi hilo principal.",
                "Soñando con compiladores veloces y cero NullPointers..."
            )
            "STUDYING" -> listOf(
                "¡Shhh! Estoy optimizando algoritmos en mi cerebro.",
                "Compilando... codeando a 1000 WPM.",
                "Siento cómo se incrementa mi sinapsis neuronal binaria."
            )
            "SICK" -> listOf(
                "Error 500: Necesito desbuguear urgente. ¡Dame una píldora!",
                "Demasiados bugs acumulados en mi stack... me siento mal.",
                "Siento mi CPU sobrecalentada. ¿Podemos repasar un poco?"
            )
            "SAD" -> listOf(
                "Tengo flojera... me siento un poco depre.",
                "Mi batería de motivación está por debajo del 20%.",
                "¿Procrastinando otra vez? Mi código se llena de advertencias."
            )
            "HUNGRY" -> listOf(
                "¡NullPointerException en mi estómago! Necesito bytes.",
                "Mi caché de energía está vacía, ¿me das de comer?",
                "Sin comida, mi rendimiento cae a O(n^2)."
            )
            "EXCITED" -> listOf(
                "¡Wiii! ¡Mi código es O(1) y mi corazón también!",
                "¡Nivel de felicidad al MÁXIMO! Gracias por quererme.",
                "¡Siento que podría compilar el kernel de Linux en 1 segundo!"
            )
            else -> listOf(
                "¡Compilar sin advertencias es mi pasión!",
                "¿Listo para tirar unas líneas de código limpias hoy?",
                "¡Siento el poder de un refactor exitoso!",
                "Me agradas, haces que mi arquitectura sea modular y sólida."
            )
        }
        quotes.random()
    }

    Card(
        shape = cardShape,
        border = BorderStroke(appTheme.borderWidth, statusColor.copy(alpha = 0.8f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pet_viewport_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.testTag("pet_name_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Renombrar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onRenameClick() }
                        )
                    }
                    Text(
                        text = "Especialista: ${state.language}",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentHearts = (state.health / 20f).toInt().coerceIn(0, 5)
                        for (i in 1..5) {
                            val isFilled = i <= currentHearts
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Corazón $i",
                                tint = if (isFilled) MaterialTheme.colorScheme.error else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    color = statusColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, statusColor),
                    modifier = Modifier.testTag("level_badge")
                ) {
                    Text(
                        text = "LVL ${state.level}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val infiniteTransition = rememberInfiniteTransition(label = "pet_animation")

            val offsetY by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "EXCITED" || state.currentStatus == "HAPPY") -10f else if (state.currentStatus == "SLEEPING") -5f else 0f,
                targetValue = if (state.currentStatus == "EXCITED" || state.currentStatus == "HAPPY") 10f else if (state.currentStatus == "SLEEPING") 5f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "EXCITED") 300 else if (state.currentStatus == "HAPPY") 600 else if (state.currentStatus == "SLEEPING") 2000 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_offset_y"
            )

            val offsetX by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "SICK") -5f else 0f,
                targetValue = if (state.currentStatus == "SICK") 5f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "SICK") 100 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_offset_x"
            )

            val scale by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "HUNGRY") 0.95f else 1f,
                targetValue = if (state.currentStatus == "HUNGRY") 1.05f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "HUNGRY") 1000 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(12.dp)))
                    .background(
                        Brush.verticalGradient(
                            colors = if (appTheme.usesGradients && appTheme.gradientColors.isNotEmpty()) {
                                appTheme.gradientColors
                            } else {
                                listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp, y = offsetY.dp)
                        .scale(bounceScale.value * scale)
                ) {
                    Image(
                        painter = painterResource(id = petImageRes),
                        contentDescription = "Estado: ${state.currentStatus}",
                        modifier = Modifier
                            .offset(y = bounceOffsetY.value.dp)
                            .size(170.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onPetTap() },
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(statusColor, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = state.currentStatus,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (showHeart) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Amor",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = heartOffsetY.value.dp)
                                .graphicsLayer(alpha = heartAlpha.value)
                                .size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = appTheme.cornerRadius.coerceAtMost(12.dp), bottomStart = appTheme.cornerRadius.coerceAtMost(12.dp), bottomEnd = appTheme.cornerRadius.coerceAtMost(12.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Mensaje",
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = randomQuote,
                        fontSize = 13.sp,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val currentLevelRequiredXp = state.level * 100
            val xpProgress = (state.xp.toFloat() / currentLevelRequiredXp.toFloat()).coerceIn(0f, 100f)
            val animatedXpProgress by animateFloatAsState(targetValue = xpProgress)

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "XP: ${state.xp} / $currentLevelRequiredXp",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(xpProgress * 100).toInt()}%",
                        fontSize = 11.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { animatedXpProgress },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeterItem(
                    label = "Vida", value = state.health,
                    icon = Icons.Default.Favorite, activeColor = MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("health_bar")
                )
                MeterItem(
                    label = "Alimento", value = state.hunger,
                    icon = Icons.Default.Restaurant, activeColor = appTheme.accent,
                    trackColor = appTheme.accent.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("hunger_bar")
                )
                MeterItem(
                    label = "Energía", value = state.energy,
                    icon = Icons.Default.FlashOn, activeColor = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("energy_bar")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = appTheme.accent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.bytes} B", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, fontSize = 13.sp, color = appTheme.accent)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.streak} días", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = { viewModel.toggleSleep() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("action_toggle_sleep")
                ) {
                    Icon(
                        imageVector = if (state.currentStatus == "SLEEPING") Icons.Default.WbSunny else Icons.Default.NightsStay,
                        contentDescription = "Dormir/Despertar",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (state.currentStatus == "SLEEPING") "Despertar" else "Dormir",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { viewModel.petThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Acariciar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Acariciar", fontSize = 10.sp, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = { viewModel.cleanThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = "Limpiar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Limpiar", fontSize = 10.sp, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = { onRenameClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Ajustar", fontSize = 10.sp, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = appTheme.accent),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = "Jugar", tint = appTheme.onPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Jugar", color = appTheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 10.sp, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
