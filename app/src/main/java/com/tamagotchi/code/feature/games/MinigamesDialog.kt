package com.tamagotchi.code.feature.games

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun MinigamesDialog(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onDismiss: () -> Unit
) {
    var selectedGame by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF0C100D),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedGame == null) stringResource(R.string.mg_header) else stringResource(R.string.mg_detail),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = {
                            if (selectedGame != null) {
                                selectedGame = null
                            } else {
                                onDismiss()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedGame != null) Icons.AutoMirrored.Filled.KeyboardArrowLeft else Icons.Default.Close,
                            contentDescription = stringResource(R.string.mg_back),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                when (selectedGame) {
                    null -> {
                        Text(
                            text = stringResource(R.string.mg_intro, state.name),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.LightGray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameOptionCard(
                            title = stringResource(R.string.mg_binary_title),
                            description = stringResource(R.string.mg_binary_desc),
                            icon = Icons.Default.Code,
                            onClick = { selectedGame = "BINARY_GUESS" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = stringResource(R.string.mg_smasher_title),
                            description = stringResource(R.string.mg_smasher_desc),
                            icon = Icons.Default.BugReport,
                            onClick = { selectedGame = "BUG_SMASHER" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = stringResource(R.string.mg_rps_title),
                            description = stringResource(R.string.mg_rps_desc),
                            icon = Icons.Default.Security,
                            onClick = { selectedGame = "ROCK_PAPER_SCI" }
                        )
                    }
                    "BINARY_GUESS" -> {
                        BinaryGuessGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                    "BUG_SMASHER" -> {
                        BugSmasherGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                    "ROCK_PAPER_SCI" -> {
                        RockPaperSciGame(
                            state = state,
                            viewModel = viewModel,
                            onFinish = { selectedGame = null }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF151D16),
        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF81C784),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
