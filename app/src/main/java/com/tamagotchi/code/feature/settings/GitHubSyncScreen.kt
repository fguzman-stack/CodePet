package com.tamagotchi.code.feature.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun GitHubSyncScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val stats by viewModel.githubStats.collectAsStateWithLifecycle()
    val loading by viewModel.githubSyncLoading.collectAsStateWithLifecycle()
    var token by remember { mutableStateOf("") }
    var showToken by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF81C784))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ">>> GITHUB STATS SYNC",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )
        }

        Text(
            text = "Conecta tu GitHub y gana XP extra por tus contribuciones del día.",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            label = { Text("GitHub Personal Access Token", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
            visualTransformation = if (showToken) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF81C784),
                unfocusedBorderColor = Color(0xFF2E7D32).copy(alpha = 0.5f),
                cursorColor = Color(0xFF81C784)
            ),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 11.sp),
            trailingIcon = {
                IconButton(onClick = { showToken = !showToken }) {
                    Icon(
                        if (showToken) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.fetchGitHubStats(token.trim()) },
            enabled = token.isNotBlank() && !loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SINCRONIZAR",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        stats?.let { s ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF151D16)),
                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF81C784))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = s.username,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    HorizontalDivider(color = Color(0xFF2E7D32).copy(alpha = 0.3f))
                    StatRow("Repos públicos", s.publicRepos.toString(), Icons.Default.Folder)
                    StatRow("Commits hoy", s.todayCommits.toString(), Icons.Default.Commit)
                    StatRow("PRs hoy", s.todayPRs.toString(), Icons.Default.CallSplit)
                    StatRow("Issues hoy", s.todayIssues.toString(), Icons.Default.BugReport)
                    HorizontalDivider(color = Color(0xFF2E7D32).copy(alpha = 0.3f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "XP Bonus: +${s.xpBonus}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }
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

@Composable
private fun StatRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


