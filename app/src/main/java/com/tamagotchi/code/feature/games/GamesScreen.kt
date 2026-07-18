package com.tamagotchi.code.feature.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBugHunt: () -> Unit,
    onNavigateToGitRescue: () -> Unit,
    onNavigateToRefactorRush: () -> Unit,
    onNavigateToCodeReview: () -> Unit = {},
    onNavigateToHackathon: () -> Unit = {}
) {
    var showClassicDialog by remember { mutableStateOf(false) }
    val gameCooldowns by viewModel.gameCooldowns.collectAsStateWithLifecycle()

    fun canPlay(gameId: String): Boolean {
        val lastPlayed = gameCooldowns[gameId] ?: 0L
        return System.currentTimeMillis() - lastPlayed >= 24 * 60 * 60 * 1000
    }

    fun handleGameClick(gameId: String, navigate: () -> Unit) {
        if (canPlay(gameId)) {
            navigate()
        }
    }

    if (showClassicDialog) {
        val petState = viewModel.petState.collectAsState().value
        if (petState != null) {
            MinigamesDialog(
                state = petState,
                viewModel = viewModel,
                onDismiss = { showClassicDialog = false }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.games_title), fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Game 1: Bug Hunt
            GameCard(
                title = stringResource(R.string.games_bug_hunt_title),
                description = stringResource(R.string.games_bug_hunt_desc),
                canPlay = canPlay("bug_hunt"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("bug_hunt", onNavigateToBugHunt) }
            )

            // Game 2: Git Rescue
            GameCard(
                title = stringResource(R.string.games_git_rescue_title),
                description = stringResource(R.string.games_git_rescue_desc),
                canPlay = canPlay("git_rescue"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("git_rescue", onNavigateToGitRescue) }
            )

            // Game 3: Refactor Rush
            GameCard(
                title = stringResource(R.string.games_refactor_rush_title),
                description = stringResource(R.string.games_refactor_rush_desc),
                canPlay = canPlay("refactor_rush"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("refactor_rush", onNavigateToRefactorRush) }
            )

            // Game 4: Code Review
            GameCard(
                title = "Code Review",
                description = "Revisa snippets de código y detecta bugs.",
                canPlay = canPlay("code_review"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("code_review", onNavigateToCodeReview) }
            )

            // Game 5: Hackatón Semanal
            GameCard(
                title = "Hackatón Semanal",
                description = "Desafío de algoritmo de fin de semana.",
                canPlay = canPlay("hackathon"),
                onCooldownText = stringResource(R.string.games_cooldown),
                onClick = { handleGameClick("hackathon", onNavigateToHackathon) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Classic Arcade Section
            Text(
                text = stringResource(R.string.games_classic_arcade),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.games_classic_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Button(
                onClick = { showClassicDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(R.string.games_play))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GameCard(
    title: String,
    description: String,
    canPlay: Boolean,
    onCooldownText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (canPlay) onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (canPlay) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        enabled = canPlay
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { if (canPlay) onClick() },
                modifier = Modifier.align(Alignment.End),
                enabled = canPlay
            ) {
                Text(if (canPlay) stringResource(R.string.games_play) else onCooldownText)
            }
        }
    }
}
