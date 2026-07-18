package com.tamagotchi.code.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.components.DailyRewardDialog
import com.tamagotchi.code.ui.components.CodeCardDialog
import com.tamagotchi.code.ui.components.ViewportCard
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun HomeScreen(
    viewModel: PetViewModel,
    onRenameClick: () -> Unit,
    onPlayClick: () -> Unit,
    onReviveWithAd: () -> Unit = {}
) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val state = petState
    LaunchedEffect(Unit) { viewModel.checkPendingCommit() }
    if (state != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            ViewportCard(
                state = state,
                viewModel = viewModel,
                onRenameClick = onRenameClick,
                onPlayClick = onPlayClick
            )

            OutlinedButton(
                onClick = { viewModel.openDailyRewardDialog() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        Icons.Filled.AllInbox,
                        contentDescription = null,
                        tint = if (viewModel.isDailyRewardClaimedToday.value)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (viewModel.isDailyRewardClaimedToday.value) "RECOMPENSAS DIARIAS (RECLAMADO)" else "RECOMPENSAS DIARIAS (¡DISPONIBLE!)",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (viewModel.isDailyRewardClaimedToday.value)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (viewModel.showOfflineRewardDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissOfflineRewardDialog() },
                title = { Text("Sesión completada mientras estabas fuera") },
                text = { Text("¡Felicidades! Has ganado ${viewModel.offlineRewardXp.value} XP y ${viewModel.offlineRewardBytes.value} Bytes por tu sesión de Focus.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissOfflineRewardDialog() }) {
                        Text("Aceptar")
                    }
                }
            )
        }

        if (viewModel.showDailyRewardDialog.value) {
            DailyRewardDialog(
                viewModel = viewModel,
                onDismissRequest = { viewModel.dismissDailyRewardDialog() }
            )
        }

        if (viewModel.showDeathDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeathDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            Icons.Filled.Dangerous,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "${state?.name ?: "Codey"} ha muerto",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "Tu mascota ha fallecido por falta de cuidados.\n\n" +
                                    "Pero no todo está perdido... puedes revivirla:",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Save, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(
                                "Revivir por ${viewModel.deathReviveCost.value} Bytes",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Tv, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                            Text(
                                "Revivir viendo un anuncio",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                confirmButton = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.reviveWithBytes() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            enabled = (state?.bytes ?: 0) >= viewModel.deathReviveCost.value
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Filled.Save, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    "Revivir (${viewModel.deathReviveCost.value} Bytes)",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onReviveWithAd,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Filled.Tv, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Text(
                                    "Revivir con anuncio",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            )
        }

        if (viewModel.showCodeCardDialog.value && viewModel.currentCodeCard.value != null) {
            CodeCardDialog(
                card = viewModel.currentCodeCard.value!!,
                onDismiss = { viewModel.dismissCodeCardDialog() }
            )
        }

        if (viewModel.showCommitDialog.value) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissCommitDialog() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            Icons.Default.AllInbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Codey hizo commit",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column {
                        Text(
                            "Resumen de ayer:",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFF1B1B2F),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = viewModel.pendingCommitMessage.value,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = Color(0xFF00FF41),
                                modifier = Modifier.padding(12.dp),
                                lineHeight = 20.sp
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissCommitDialog() }) {
                        Text("Fusionar (Aceptar)", fontFamily = FontFamily.Monospace)
                    }
                }
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
        }
    }
}