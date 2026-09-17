package com.tamagotchi.code.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.tamagotchi.code.ui.viewmodel.DailyReward
import com.tamagotchi.code.ui.viewmodel.dailyRewardsList

private val richDarkBg = Color(0xFF1A1A2E)
private val richSurface = Color(0xFF16213E)
private val darkCardBg = Color(0xFF0F0F23)

private val dayColors = mapOf(
    1 to Color(0xFF4CAF50),
    2 to Color(0xFF42A5F5),
    3 to Color(0xFFFFA726),
    4 to Color(0xFFEF5350),
    5 to Color(0xFFAB47BC),
    6 to Color(0xFF26A69A),
    7 to Color(0xFFFFD700)
)

private val dayIcons = mapOf(
    1 to Icons.Default.Code,
    2 to Icons.Default.DataObject,
    3 to Icons.Default.LocalCafe,
    4 to Icons.Default.Repeat,
    5 to Icons.Default.BugReport,
    6 to Icons.Default.CheckCircle,
    7 to Icons.Default.AutoAwesome
)

@Composable
private fun DayIcon(reward: DailyReward, size: Int = 28) {
    val color = dayColors[reward.day] ?: Color.White
    val icon = dayIcons[reward.day] ?: Icons.Default.Save

    Box(
        modifier = Modifier
            .size(size.dp)
            .background(
                color.copy(alpha = 0.15f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size((size * 0.55f).dp)
        )
    }
}

@Composable
fun DailyRewardDialog(
    viewModel: PetViewModel,
    onDismissRequest: () -> Unit
) {
    val currentDay = viewModel.nextClaimableDay.value
    val rewards = dailyRewardsList
    val isClaimedToday = viewModel.isDailyRewardClaimedToday.value

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = richDarkBg,
            tonalElevation = 0.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (isClaimedToday) Color(0xFF4CAF50).copy(alpha = 0.15f)
                            else dayColors[currentDay]?.copy(alpha = 0.15f) ?: Color.White.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isClaimedToday) Icons.Default.CheckCircle else Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isClaimedToday) Color(0xFF4CAF50) else dayColors[currentDay] ?: Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(if (isClaimedToday) R.string.dr_claimed_title else R.string.dr_dialog_title),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(if (isClaimedToday) R.string.dr_claimed_sub else R.string.dr_open_sub),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Days 1-3 row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rewards.take(3).forEach { reward ->
                        RewardCard(
                            reward = reward,
                            isClaimed = reward.day < currentDay,
                            isCurrent = currentDay == reward.day && !isClaimedToday,
                            isLocked = reward.day > currentDay,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Days 4-6 row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rewards.drop(3).take(3).forEach { reward ->
                        RewardCard(
                            reward = reward,
                            isClaimed = reward.day < currentDay,
                            isCurrent = currentDay == reward.day && !isClaimedToday,
                            isLocked = reward.day > currentDay,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day 7 - Full width Mega Pack
                val day7 = rewards.last()
                MegaPackCard(
                    reward = day7,
                    isClaimed = day7.day < currentDay,
                    isCurrent = currentDay == day7.day && !isClaimedToday,
                    isLocked = day7.day > currentDay
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!isClaimedToday && currentDay in 1..7) {
                            viewModel.claimDailyReward()
                        } else {
                            onDismissRequest()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isClaimedToday) Color(0xFF4CAF50)
                        else dayColors[currentDay] ?: MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(if (isClaimedToday) R.string.dr_close_btn else R.string.dr_claim_btn),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: DailyReward,
    isClaimed: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean,
    modifier: Modifier = Modifier
) {
    val accentColor = dayColors[reward.day] ?: Color.White

    val border = when {
        isCurrent -> BorderStroke(2.dp, accentColor)
        isClaimed -> BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
        else -> BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    }

    val bg = when {
        isCurrent -> richSurface
        isClaimed -> darkCardBg.copy(alpha = 0.5f)
        else -> darkCardBg
    }

    val alpha = if (isLocked) 0.3f else if (isClaimed) 0.5f else 1f

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        border = border,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.dr_day, reward.day),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = alpha * 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            DayIcon(reward = reward, size = 32)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "+${reward.bytes} B",
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = alpha)
            )
            Text(
                text = "+${reward.xp} XP",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = alpha * 0.6f)
            )

            if (isClaimed) {
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun MegaPackCard(
    reward: DailyReward,
    isClaimed: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean
) {
    val accentColor = Color(0xFFFFD700)

    val border = when {
        isCurrent -> BorderStroke(2.dp, accentColor)
        isClaimed -> BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
        else -> BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
    }

    val bg = when {
        isCurrent -> richSurface
        isClaimed -> darkCardBg.copy(alpha = 0.5f)
        else -> darkCardBg
    }

    val alpha = if (isLocked) 0.3f else if (isClaimed) 0.5f else 1f

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = bg,
        border = border,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayIcon(reward = reward, size = 40)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.dr_day7_mega),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = accentColor.copy(alpha = alpha)
                )
                Text(
                    text = stringResource(reward.titleRes),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = alpha * 0.7f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Diamond,
                            contentDescription = null,
                            tint = accentColor.copy(alpha = alpha),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${reward.bytes} B",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = accentColor.copy(alpha = alpha)
                        )
                    }
                    Text(
                        text = "+${reward.xp} XP",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = accentColor.copy(alpha = alpha * 0.8f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFEF5350).copy(alpha = alpha),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.dr_health_amount, reward.healthRestore.toInt()),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White.copy(alpha = alpha * 0.6f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color(0xFFFFA726).copy(alpha = alpha),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.dr_energy_amount, reward.energyRestore.toInt()),
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White.copy(alpha = alpha * 0.6f)
                        )
                    }
                }
            }

            if (isClaimed) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
