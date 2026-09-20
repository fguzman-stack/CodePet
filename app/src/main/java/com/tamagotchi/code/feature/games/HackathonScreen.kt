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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.HackathonData
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.tamagotchi.code.ui.theme.readableOnBackground

@Composable
fun HackathonScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val hackathonState by viewModel.hackathonState.collectAsStateWithLifecycle()
    val data = hackathonState ?: HackathonData()

    var quizOpen by remember { mutableStateOf(false) }
    var quizResult by remember { mutableStateOf<Boolean?>(null) }
    val startTime = remember { System.currentTimeMillis() }
    val quizOptions = remember {
        listOf(
            R.string.hack_opt_own to true,
            R.string.hack_opt_sorted to false,
            R.string.hack_opt_collections to false,
            R.string.hack_opt_sort to false
        ).shuffled()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFD700))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.hack_header),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = readableOnBackground(Color(0xFF81C784))
            )
        }

        Text(
            text = stringResource(R.string.hack_desc),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            color = readableOnBackground(Color.Gray)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF151D16)),
            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.hack_this_week),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF81C784)
                )
                Text(
                    text = stringResource(R.string.hack_challenge),
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
                    text = stringResource(R.string.hack_attempts, 3 - data.attempts),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (data.attempts < 3) Color(0xFF81C784) else Color(0xFFEF5350)
                )
                if (data.bestTimeMs < Long.MAX_VALUE) {
                    Text(
                        text = stringResource(R.string.hack_best_time, data.bestTimeMs / 1000),
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

        if (quizResult != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (quizResult == true) Color(0xFF1B5E20).copy(alpha = 0.35f) else Color(0xFFB71C1C).copy(alpha = 0.3f),
                border = BorderStroke(1.dp, if (quizResult == true) Color(0xFF81C784) else Color(0xFFEF5350)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(if (quizResult == true) R.string.hack_expl_win else R.string.hack_wrong),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = readableOnBackground(if (quizResult == true) Color(0xFF81C784) else Color(0xFFEF5350)),
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else if (data.attempts < 3) {
            if (!quizOpen) {
                Button(
                    onClick = { quizOpen = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.hack_solve),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.hack_quiz_prompt),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = readableOnBackground(Color(0xFF81C784))
                )
                quizOptions.forEach { (optionRes, isCorrect) ->
                    Button(
                        onClick = {
                            if (isCorrect) {
                                viewModel.submitHackathonSolution(System.currentTimeMillis() - startTime)
                            } else {
                                viewModel.consumeHackathonAttempt()
                            }
                            quizResult = isCorrect
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "> " + stringResource(optionRes),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color(0xFF81C784),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.hack_reward),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = readableOnBackground(Color(0xFFFFB74D)),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = stringResource(R.string.hack_no_attempts),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = readableOnBackground(Color(0xFFEF5350)),
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
            Text(stringResource(R.string.game_back), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}
