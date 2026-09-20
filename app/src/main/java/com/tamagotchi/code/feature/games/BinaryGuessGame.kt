package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

private fun toBinary4(n: Int): String = Integer.toBinaryString(n).padStart(4, '0')

private fun binaryOptionsFor(n: Int): List<Pair<String, Boolean>> {
    val correct = toBinary4(n)
    val distractors = linkedSetOf(
        correct.reversed(),
        toBinary4(n + 1),
        toBinary4((n - 1).coerceAtLeast(0)),
        correct.map { if (it == '0') '1' else '0' }.joinToString("")
    ).filter { it != correct && it.length == 4 }
    val picked = distractors.shuffled().take(3).toMutableList()
    while (picked.size < 3) {
        val candidate = toBinary4((0..15).random())
        if (candidate != correct && candidate !in picked) picked.add(candidate)
    }
    return (picked.map { it to false } + (correct to true)).shuffled()
}

@Composable
fun BinaryGuessGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var round by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var currentDecimal by remember { mutableStateOf((1..15).random()) }
    var currentOptions by remember { mutableStateOf(binaryOptionsFor(currentDecimal)) }
    var feedbackRes by remember { mutableStateOf<Int?>(null) }
    var showNextButton by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    fun nextRound() {
        currentDecimal = (1..15).random()
        currentOptions = binaryOptionsFor(currentDecimal)
        feedbackRes = null
        showNextButton = false
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.binary_header, round),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Black, RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFF2E7D32), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGameOver) stringResource(R.string.binary_fin) else "$currentDecimal",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF81C784)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (feedbackRes != null) {
                stringResource(feedbackRes!!, currentDecimal, toBinary4(currentDecimal))
            } else {
                stringResource(R.string.binary_prompt)
            },
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isGameOver) {
            val bytesReward = score * 4
            val healthReward = score * 3f
            Text(
                text = stringResource(R.string.binary_gameover, score, bytesReward, healthReward.toInt()),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
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
                Text(stringResource(R.string.game_claim_rewards), fontFamily = FontFamily.Monospace)
            }
        } else if (showNextButton) {
            Button(
                onClick = {
                    if (round < 5) {
                        round += 1
                        nextRound()
                    } else {
                        isGameOver = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.binary_next_round), fontFamily = FontFamily.Monospace)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                currentOptions.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowOptions.forEach { (binary, isCorrectOption) ->
                            Button(
                                onClick = {
                                    if (isCorrectOption) {
                                        score += 1
                                        feedbackRes = R.string.binary_correct
                                    } else {
                                        feedbackRes = R.string.binary_wrong
                                    }
                                    showNextButton = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text(binary, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                            }
                        }
                    }
                }
            }
        }
    }
}
