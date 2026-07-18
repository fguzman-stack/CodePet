package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import com.tamagotchi.code.data.CodeReviewData
import com.tamagotchi.code.data.CodeReviewSnippet
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun CodeReviewScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    var currentRound by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    val rounds = remember { CodeReviewData.snippets.shuffled().take(5) }
    var showResults by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf<Boolean?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }

    if (showResults) {
        CodeReviewResult(score = score, total = 5, onBack = onBack, viewModel = viewModel)
    } else {
        val snippet = rounds[currentRound]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = ">>> CODE REVIEW: Ronda ${currentRound + 1}/5",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF151D16),
                border = BorderStroke(1.dp, Color(0xFF2E7D32))
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = snippet.code,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedAnswer == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            selectedAnswer = false
                            isCorrect = !snippet.hasBug
                            if (isCorrect == true) score++
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("APROBAR", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            selectedAnswer = true
                            isCorrect = snippet.hasBug
                            if (isCorrect == true) score++
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.BugReport, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SOLICITAR CAMBIOS", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCorrect == true) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFFB71C1C).copy(alpha = 0.3f),
                    border = BorderStroke(1.dp, if (isCorrect == true) Color(0xFF81C784) else Color(0xFFEF5350))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isCorrect == true) "¡ACERTADO!" else "INCORRECTO",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (isCorrect == true) Color(0xFF81C784) else Color(0xFFEF5350)
                        )
                        Text(
                            text = snippet.explanation,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (currentRound < 4) {
                                    currentRound++
                                    selectedAnswer = null
                                    isCorrect = null
                                } else {
                                    showResults = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("CONTINUAR", fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CodeReviewResult(
    score: Int,
    total: Int,
    onBack: () -> Unit,
    viewModel: PetViewModel
) {
    val bytesEarned = score * 10
    val xpEarned = score * 5

    LaunchedEffect(Unit) {
        viewModel.completeMinigame(bytesEarned, 5f, -10f)
        viewModel.recordGamePlay("code_review")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REVISIÓN COMPLETADA",
            fontFamily = FontFamily.Monospace,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF81C784)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Puntuación: $score / $total",
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "¡Buen trabajo SR. Developer!",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "+$bytesEarned Bytes\n+$xpEarned XP",
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF81C784),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Text("VOLVER AL TERMINAL", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}
