package com.tamagotchi.code.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.ui.viewmodel.PairBuddyState

@Composable
fun PairBuddyOverlay(
    buddy: PairBuddyState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF1A1A2E).copy(alpha = 0.95f),
            border = BorderStroke(1.dp, Color(0xFFFFB74D).copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Buggy",
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${buddy.name} está aquí!",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(0xFFFFB74D)
                    )
                    Text(
                        text = "XP x${buddy.xpMultiplier} por ${buddy.remainingChallenges} reto(s) más",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
