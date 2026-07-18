package com.tamagotchi.code.feature.skills

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun WeeklyMissionsScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val missions by viewModel.weeklyMissionsState.collectAsStateWithLifecycle()
    val completedCount = missions.count { it.completed }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Assignment, contentDescription = null, tint = Color(0xFF81C784))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ">>> MISIONES SEMANALES",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )
        }

        if (completedCount == 3) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFD700).copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color(0xFFFFD700)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFD700))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BONUS COMPLETO: 500 XP + Sombrero exclusivo!",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFFFD700)
                    )
                }
            }
        }

        Text(
            text = "Completa las 3 misiones semanales para un bonus especial.",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color.Gray
        )

        missions.forEach { mission ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (mission.completed) Color(0xFF1B5E20).copy(alpha = 0.2f) else Color(0xFF151D16)
                ),
                border = BorderStroke(
                    1.dp,
                    if (mission.completed) Color(0xFF81C784) else Color(0xFF2E7D32).copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (mission.completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (mission.completed) Color(0xFF81C784) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mission.title,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = if (mission.completed) Color(0xFF81C784) else Color.White
                        )
                        Text(
                            text = mission.description,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+${mission.rewardXp} XP",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFB74D)
                        )
                        Text(
                            text = "+${mission.rewardBytes} B",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF81C784)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Text("VOLVER", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}
