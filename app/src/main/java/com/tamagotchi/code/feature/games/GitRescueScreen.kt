package com.tamagotchi.code.feature.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay

private data class GitScenario(
    val situationRes: Int,
    val optionsRes: List<Int>,
    val correctIndex: Int,
    val explanationRes: Int,
    val humorRes: Int
)

private val scenarioPool = listOf(
    GitScenario(
        situationRes = R.string.git_sit_1,
        optionsRes = listOf(
            R.string.git_opt_1a,
            R.string.git_opt_1b,
            R.string.git_opt_1c
        ),
        correctIndex = 0,
        explanationRes = R.string.git_expl_1,
        humorRes = R.string.git_humor_1
    ),
    GitScenario(
        situationRes = R.string.git_sit_2,
        optionsRes = listOf(
            R.string.git_opt_2a,
            R.string.git_opt_2b,
            R.string.git_opt_2c
        ),
        correctIndex = 1,
        explanationRes = R.string.git_expl_2,
        humorRes = R.string.git_humor_2
    ),
    GitScenario(
        situationRes = R.string.git_sit_3,
        optionsRes = listOf(
            R.string.git_opt_3a,
            R.string.git_opt_3b,
            R.string.git_opt_3c
        ),
        correctIndex = 0,
        explanationRes = R.string.git_expl_3,
        humorRes = R.string.git_humor_3
    ),
    GitScenario(
        situationRes = R.string.git_sit_4,
        optionsRes = listOf(
            R.string.git_opt_4a,
            R.string.git_opt_4b,
            R.string.git_opt_4c
        ),
        correctIndex = 0,
        explanationRes = R.string.git_expl_4,
        humorRes = R.string.git_humor_4
    ),
    GitScenario(
        situationRes = R.string.git_sit_5,
        optionsRes = listOf(
            R.string.git_opt_5a,
            R.string.git_opt_5b,
            R.string.git_opt_5c
        ),
        correctIndex = 0,
        explanationRes = R.string.git_expl_5,
        humorRes = R.string.git_humor_5
    ),
    GitScenario(
        situationRes = R.string.git_sit_6,
        optionsRes = listOf(
            R.string.git_opt_6a,
            R.string.git_opt_6b,
            R.string.git_opt_6c
        ),
        correctIndex = 1,
        explanationRes = R.string.git_expl_6,
        humorRes = R.string.git_humor_6
    ),
    GitScenario(
        situationRes = R.string.git_sit_7,
        optionsRes = listOf(
            R.string.git_opt_7a,
            R.string.git_opt_7b,
            R.string.git_opt_7c
        ),
        correctIndex = 0,
        explanationRes = R.string.git_expl_7,
        humorRes = R.string.git_humor_7
    ),
    GitScenario(
        situationRes = R.string.git_sit_8,
        optionsRes = listOf(
            R.string.git_opt_8a,
            R.string.git_opt_8b,
            R.string.git_opt_8c
        ),
        correctIndex = 2,
        explanationRes = R.string.git_expl_8,
        humorRes = R.string.git_humor_8
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitRescueScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val scenarios = remember { scenarioPool.shuffled().take(5) }

    var currentStep by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var codeyReactionRes by remember { mutableStateOf(R.string.git_initial) }
    var lastExplanationRes by remember { mutableStateOf<Int?>(null) }
    var answeredStep by remember { mutableStateOf(false) }
    var branchPosition by remember { mutableStateOf(0f) }
    var maxBranchSteps by remember { mutableStateOf(scenarios.size.toFloat()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.git_rescue_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(stringResource(R.string.game_result_title), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = score * 15
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.git_decisions, score, scenarios.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("git_rescue")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                // Branch visual progress
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(stringResource(R.string.git_progress), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            scenarios.forEachIndexed { index, _ ->
                                val isDone = index < currentStep
                                val isCurrent = index == currentStep
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = when {
                                        isDone -> MaterialTheme.colorScheme.primary
                                        isCurrent -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            color = if (isDone || isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (index < scenarios.size - 1) {
                                    Surface(
                                        modifier = Modifier
                                            .height(4.dp)
                                            .weight(1f),
                                        color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ) {}
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.codey_says, stringResource(codeyReactionRes)), modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(stringResource(R.string.git_step, currentStep + 1, scenarios.size), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(scenarios[currentStep].situationRes), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                scenarios[currentStep].optionsRes.forEachIndexed { index, optionRes ->
                    val isCorrect = answeredStep && index == scenarios[currentStep].correctIndex
                    val isWrong = answeredStep && index != scenarios[currentStep].correctIndex

                    Surface(
                        color = when {
                            isCorrect -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                            isWrong -> Color(0xFFB71C1C).copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !answeredStep) {
                                if (index == scenarios[currentStep].correctIndex) {
                                    score++
                                    codeyReactionRes = scenarios[currentStep].humorRes
                                } else {
                                    codeyReactionRes = R.string.git_wrong
                                }
                                lastExplanationRes = scenarios[currentStep].explanationRes
                                answeredStep = true
                            }
                    ) {
                        Text(
                            text = "> " + stringResource(optionRes),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                if (answeredStep && lastExplanationRes != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(lastExplanationRes!!),
                            modifier = Modifier.padding(12.dp),
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (currentStep < scenarios.size - 1) {
                            currentStep++
                            answeredStep = false
                            lastExplanationRes = null
                            codeyReactionRes = R.string.git_next
                        } else {
                            isGameOver = true
                        }
                    }) {
                        Text(if (currentStep < scenarios.size - 1) stringResource(R.string.game_next_decision) else stringResource(R.string.game_see_results))
                    }
                }
            }
        }
    }
}
