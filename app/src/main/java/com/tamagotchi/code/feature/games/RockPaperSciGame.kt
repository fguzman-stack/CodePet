package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun RockPaperSciGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var userWins by remember { mutableStateOf(0) }
    var cpuWins by remember { mutableStateOf(0) }
    var roundMessage by remember { mutableStateOf("Elige tu jugada para iniciar la ronda.") }
    var userChoice by remember { mutableStateOf<String?>(null) }
    var cpuChoice by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    val choices = listOf("Servidor", "Script", "Hacker")
    val icons = mapOf(
        "Servidor" to Icons.Default.Computer,
        "Script" to Icons.Default.Description,
        "Hacker" to Icons.Default.BugReport
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SERVIDOR, SCRIPT, HACKER (RPS)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "TÚ: $userWins | CPU: $cpuWins (Mejor de 3)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF2E7D32), RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TÚ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[userChoice] ?: Icons.Default.QuestionMark,
                    contentDescription = userChoice ?: "Pregunta",
                    tint = if (userChoice != null) Color(0xFF81C784) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(userChoice ?: "Selecciona...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }

            Text("VS", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFFEF5350))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("COMPILADOR", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[cpuChoice] ?: Icons.Default.QuestionMark,
                    contentDescription = cpuChoice ?: "Pregunta",
                    tint = if (cpuChoice != null) Color(0xFFEF5350) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(cpuChoice ?: "Esperando...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = roundMessage,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (isGameOver) {
            val playerWon = userWins >= 2
            val bytesReward = if (playerWon) 20 else 5
            val healthReward = if (playerWon) 25f else 10f
            Text(
                text = if (playerWon) "¡Felicidades! Derrotaste al compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad" else "Has perdido contra el compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 10f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                choices.forEach { choice ->
                    Button(
                        onClick = {
                            userChoice = choice
                            val selectedCpu = choices.random()
                            cpuChoice = selectedCpu

                            if (choice == selectedCpu) {
                                roundMessage = "Empate en esta ronda con $choice."
                            } else if (
                                (choice == "Servidor" && selectedCpu == "Hacker") ||
                                (choice == "Script" && selectedCpu == "Servidor") ||
                                (choice == "Hacker" && selectedCpu == "Script")
                            ) {
                                userWins += 1
                                roundMessage = "¡Ganaste la ronda! $choice vence a $selectedCpu."
                            } else {
                                cpuWins += 1
                                roundMessage = "Perdiste la ronda. $selectedCpu vence a $choice."
                            }

                            if (userWins >= 2 || cpuWins >= 2) {
                                isGameOver = true
                                roundMessage = if (userWins >= 2) "¡Has ganado la partida!" else "El compilador ha ganado la partida."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(choice, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                    }
                }
            }
        }
    }
}
