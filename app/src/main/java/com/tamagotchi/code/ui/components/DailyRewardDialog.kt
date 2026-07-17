package com.tamagotchi.code.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Add
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.tamagotchi.code.ui.viewmodel.DailyReward
import com.tamagotchi.code.ui.viewmodel.dailyRewardsList

@Composable
fun RewardCard(
    reward: DailyReward,
    isClaimed: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        isClaimed -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    val backgroundColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        isClaimed -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        isClaimed -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = { if (isCurrent) onClick() },
        enabled = isCurrent,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor),
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DÍA ${reward.day}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = contentColor
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            val icon = when {
                isClaimed -> Icons.Filled.Check
                reward.day == 7 -> Icons.Filled.Add
                reward.energyRestore > 0f -> Icons.Filled.Add
                reward.healthRestore > 0f -> Icons.Filled.Add
                else -> Icons.Filled.Save
            }
            
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "+${reward.bytes} B",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = "+${reward.xp} XP",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun Day7RewardCard(
    reward: DailyReward,
    isClaimed: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        isClaimed -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    val backgroundColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        isClaimed -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.surface
    }

    val contentColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        isClaimed -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Surface(
        onClick = { if (isCurrent) onClick() },
        enabled = isCurrent,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor),
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "DÍA 7 - ¡MEGA PACK!",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = contentColor
                )
                Text(
                    text = reward.title,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Save, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
                    Text(text = "+${reward.bytes} Bytes", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = contentColor, fontWeight = FontWeight.Bold)
                    Icon(Icons.Filled.Add, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
                    Text(text = "+${reward.xp} XP", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = contentColor, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = contentColor.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                    Text(text = "+${reward.healthRestore.toInt()} Salud", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = contentColor.copy(alpha = 0.8f))
                    Icon(Icons.Filled.Add, contentDescription = null, tint = contentColor.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                    Text(text = "+${reward.energyRestore.toInt()} Energ\u00eda", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = contentColor.copy(alpha = 0.8f))
                }
            }
            
            Icon(
                imageVector = if (isClaimed) Icons.Filled.Check else Icons.Filled.Add,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp).padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun DailyRewardDialog(
    viewModel: PetViewModel,
    onDismissRequest: () -> Unit
) {
    val currentDay = viewModel.nextClaimableDay.value
    val claimedDays = if (viewModel.isDailyRewardClaimedToday.value) setOf(currentDay) else emptySet()
    val rewards = dailyRewardsList

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recompensas Diarias",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "¡Vuelve cada día por más!",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(rewards) { reward ->
                        val isClaimed = claimedDays.contains(reward.day)
                        val isCurrent = currentDay == reward.day
                        val isLocked = reward.day > currentDay
                        
                        if (reward.day == 7) {
                            Day7RewardCard(
                                reward = reward,
                                isClaimed = isClaimed,
                                isCurrent = isCurrent,
                                isLocked = isLocked,
                                onClick = { viewModel.claimDailyReward() }
                            )
                        } else {
                            RewardCard(
                                reward = reward,
                                isClaimed = isClaimed,
                                isCurrent = isCurrent,
                                isLocked = isLocked,
                                onClick = { viewModel.claimDailyReward() }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}