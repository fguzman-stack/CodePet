package com.tamagotchi.code.feature.games

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.ui.viewmodel.HackathonData
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlin.math.min

@Composable
fun HackathonScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val hackathonState by viewModel.hackathonState.collectAsStateWithLifecycle()
    val data = hackathonState ?: HackathonData()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFD700))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ">>> HACKATHON SEMANAL",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )
        }

        Text(
            text = "Cada fin de semana un desafío especial de algoritmo.",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF151D16)),
            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Desafío de esta semana:",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF81C784)
                )
                Text(
                    text = "Ordena este array sin usar sort()\n\nEntrada: [3, 7, 1, 9, 4, 2, 8, 5, 6]\nSalida esperada: [1, 2, 3, 4, 5, 6, 7, 8, 9]",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    lineHeight = 16.sp
                )
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B2F)),
            border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Intentos restantes: ${3 - data.attempts} / 3",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (data.attempts < 3) Color(0xFF81C784) else Color(0xFFEF5350)
                )
                if (data.bestTimeMs < Long.MAX_VALUE) {
                    Text(
                        text = "Mejor tiempo: ${data.bestTimeMs / 1000}s",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                LinearProgressIndicator(
                    progress = { (3 - data.attempts) / 3f },
                    color = if (data.attempts < 3) Color(0xFF81C784) else Color(0xFFEF5350),
                    trackColor = Color(0xFF2E7D32).copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth().height(6.dp)
                )
            }
        }

        if (data.attempts < 3) {
            Button(
                onClick = {
                    viewModel.submitHackathonSolution(data.attempts + 1, 30000L)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SOLUCIONAR DESAFÍO",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Recompensa: 200 XP + 500 Bytes + Skin exclusiva temporal",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color(0xFFFFB74D),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "Sin intentos restantes. ¡Espera al próximo hackathon!",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color(0xFFEF5350),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

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
