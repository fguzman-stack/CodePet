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
fun SeasonPassScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val passState by viewModel.seasonPassState.collectAsStateWithLifecycle()
    val data = passState ?: return

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color(0xFF81C784))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ">>> PASE DE TEMPORADA",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )
        }

        Text(
            text = "Gana XP de pase completando actividades. 20 niveles de recompensas!",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color.Gray
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF151D16)),
            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "NIVEL ${data.level} / 20",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                LinearProgressIndicator(
                    progress = { data.level / 20f },
                    color = Color(0xFF81C784),
                    trackColor = Color(0xFF2E7D32).copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth().height(8.dp)
                )
                Text(
                    text = "XP del pase: ${data.xp}/100",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                if (!data.premium) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PASE PREMIUM: Skins exclusivas + sombreros raros + XP boost",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Text(
            text = "Recompensas del pase:",
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF81C784)
        )

        val rewards = listOf(
            "Nivel 1: 50 Bytes",
            "Nivel 5: Sombrero básico",
            "Nivel 10: 200 XP",
            "Nivel 15: Carta de código rara",
            "Nivel 20: Skin legendaria!"
        )

        rewards.forEachIndexed { idx, reward ->
            val unlocked = data.level > idx * 5
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (unlocked) Color(0xFF1B5E20).copy(alpha = 0.2f) else Color(0xFF151D16),
                border = BorderStroke(
                    1.dp,
                    if (unlocked) Color(0xFF81C784) else Color(0xFF2E7D32).copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (unlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (unlocked) Color(0xFF81C784) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = reward,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = if (unlocked) Color.White else Color.Gray
                    )
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
