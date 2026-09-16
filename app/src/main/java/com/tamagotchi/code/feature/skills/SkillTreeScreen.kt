package com.tamagotchi.code.feature.skills

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.tamagotchi.code.ui.viewmodel.SkillNodeData

@Composable
fun SkillTreeScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val skills by viewModel.skillTreeState.collectAsStateWithLifecycle()
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val xp = petState?.xp ?: 0

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountTree, contentDescription = null, tint = Color(0xFF81C784))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.skill_header),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF81C784)
            )
        }

        Text(
            text = stringResource(R.string.skill_desc),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color.Gray
        )

        Text(
            text = stringResource(R.string.skill_xp_available, xp),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFB74D)
        )

        // Skill Tree Canvas
        Canvas(
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) {
            val nodeCount = skills.size
            if (nodeCount > 1) {
                val spacing = size.width / (nodeCount - 1)
                val yCenter = size.height / 2
                val path = Path()
                for (i in 0 until nodeCount) {
                    val x = i * spacing
                    if (i == 0) path.moveTo(x, yCenter) else path.lineTo(x, yCenter)
                }
                drawPath(path, Color(0xFF2E7D32).copy(alpha = 0.5f), style = Stroke(2f))
            }
        }

        skills.forEach { skill ->
            val canAfford = xp >= (skill.currentTier + 1) * 100
            val isMaxed = skill.currentTier >= skill.maxTier
            val progress = skill.currentTier.toFloat() / skill.maxTier

            val icon: ImageVector = when (skill.icon) {
                "Star" -> Icons.Default.Star
                else -> Icons.Default.TouchApp
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMaxed) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFF151D16)
                ),
                border = BorderStroke(1.dp, if (isMaxed) Color(0xFF81C784) else Color(0xFF2E7D32).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = skill.name,
                        tint = if (isMaxed) Color(0xFFFFD700) else Color(0xFF81C784),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = skill.name,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Text(
                            text = skill.description,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            color = if (isMaxed) Color(0xFFFFD700) else Color(0xFF81C784),
                            trackColor = Color(0xFF2E7D32).copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth().height(4.dp)
                        )
                        Text(
                            text = stringResource(R.string.skill_level, skill.currentTier, skill.maxTier),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = Color.Gray
                        )
                    }

                    if (!isMaxed) {
                        val cost = (skill.currentTier + 1) * 100
                        Button(
                            onClick = { viewModel.unlockSkillNode(skill.id) },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.skill_cost, cost),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.skill_maxed),
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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
