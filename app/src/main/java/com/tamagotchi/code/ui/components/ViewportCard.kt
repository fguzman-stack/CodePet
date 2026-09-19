package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import com.tamagotchi.code.ui.theme.LocalReduceMotion
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import androidx.compose.runtime.collectAsState
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
    val heartOffsetY = remember { Animatable(0f) }
    val heartAlpha = remember { Animatable(0f) }
    var showHeart by remember { mutableStateOf(false) }

    val appTheme = LocalAppTheme.current
    val reduceMotion = LocalReduceMotion.current
    val cardShape = RoundedCornerShape(appTheme.cornerRadius)
    

    fun onPetTap() {
        viewModel.petThePet()
        viewModel.soundManager.playClick()
        scope.launch {
            showHeart = true
            heartOffsetY.snapTo(0f)
            heartAlpha.snapTo(1f)
            launch { heartOffsetY.animateTo(-110f, tween(PetAnimationConfig.heartDurationMs(reduceMotion))); heartAlpha.animateTo(0f, tween(PetAnimationConfig.heartDurationMs(reduceMotion))) }
            delay((PetAnimationConfig.heartDurationMs(reduceMotion) + 120).toLong())
            showHeart = false
        }

    }

    val statusColor = when (state.currentStatus) {
        "SLEEPING" -> Color(0xFF64B5F6)
        "STUDYING" -> MaterialTheme.colorScheme.primary
        "SICK" -> MaterialTheme.colorScheme.error
        "SAD" -> Color(0xFF90A4AE)
        "HUNGRY" -> Color(0xFFFFB74D)
        "EXCITED" -> Color(0xFFFF80AB)
        "DEAD" -> Color(0xFF546E7A)
        else -> MaterialTheme.colorScheme.secondary
    }

    val quoteArrayRes = when (state.currentStatus) {
        "SLEEPING" -> R.array.quote_sleeping
        "STUDYING" -> R.array.quote_studying
        "SICK" -> R.array.quote_sick
        "SAD" -> R.array.quote_sad
        "HUNGRY" -> R.array.quote_hungry
        "EXCITED" -> R.array.quote_excited
        "DEAD" -> R.array.quote_dead
        else -> R.array.quote_happy
    }
    val statusQuotes = stringArrayResource(quoteArrayRes)
    val randomQuote = remember(quoteArrayRes, state.xp) { statusQuotes.random() }

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
                            color = if (appTheme.name == "Matrix Green") appTheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("pet_name_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.cd_rename),
                            tint = statusColor,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onRenameClick() }
                        )
                    }
                    Text(
                        text = stringResource(R.string.viewport_specialist, state.language),
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
                                contentDescription = stringResource(R.string.cd_heart, i),
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
                        text = stringResource(R.string.viewport_level, state.level),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                Box {
                    CodeySprite(
                        status = state.currentStatus,
                        level = state.level,
                        isDead = state.isDead,
                        celebrationTrigger = viewModel.celebrationTrigger,
                        learningEventTrigger = viewModel.learningEventTrigger,
                        onClick = { onPetTap() }
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
                            contentDescription = stringResource(R.string.cd_love),
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
                        contentDescription = stringResource(R.string.cd_message),
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
                        text = stringResource(R.string.viewport_xp_progress, state.xp, currentLevelRequiredXp),
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = animatedXpProgress)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeterItem(
                    label = stringResource(R.string.meter_health), value = state.health,
                    icon = Icons.Default.Favorite, activeColor = MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("health_bar")
                )
                MeterItem(
                    label = stringResource(R.string.meter_food), value = state.hunger,
                    icon = Icons.Default.Restaurant, activeColor = appTheme.accent,
                    trackColor = appTheme.accent.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("hunger_bar")
                )
                MeterItem(
                    label = stringResource(R.string.meter_energy), value = state.energy,
                    icon = Icons.Default.FlashOn, activeColor = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("energy_bar")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(6.dp))

            val pairBuddy = viewModel.pairBuddyState.collectAsState().value
            pairBuddy?.let { buddy ->
                PairBuddyOverlay(buddy = buddy, modifier = Modifier.padding(bottom = 6.dp))
            }

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
                    Text(pluralStringResource(R.plurals.streak_days, state.streak, state.streak), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
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
                        contentDescription = stringResource(R.string.cd_sleep_toggle),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        stringResource(if (state.currentStatus == "SLEEPING") R.string.viewport_wake else R.string.viewport_sleep),
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
                    Icon(Icons.Default.Pets, contentDescription = stringResource(R.string.action_pet), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(stringResource(R.string.action_pet), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = { viewModel.cleanThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = stringResource(R.string.action_clean), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(stringResource(R.string.action_clean), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = stringResource(R.string.action_play), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(stringResource(R.string.action_play), fontWeight = FontWeight.Bold, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
