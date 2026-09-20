package com.tamagotchi.code.feature.skills

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import com.tamagotchi.code.ui.theme.readableOnBackground

@Composable
fun SeasonPassScreen(
    viewModel: PetViewModel,
    onBack: () -> Unit
) {
    val passState by viewModel.seasonPassState.collectAsStateWithLifecycle()
    val data = passState ?: return

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color(0xFF81C784))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.season_header),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = readableOnBackground(Color(0xFF81C784))
            )
        }

        Text(
            text = stringResource(R.string.season_desc),
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = readableOnBackground(Color.Gray)
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF151D16)),
            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.season_level, data.level),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                LinearProgressIndicator(
                    progress = { data.level / 20f },
                    color = Color(0xFF81C784),
                    trackColor = Color(0xFF2E7D32).copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth().height(8.dp)
                )
                Text(
                    text = stringResource(R.string.season_xp, data.xp),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                if (!data.premium) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFFFFD700)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.season_premium),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Text(
            text = stringResource(R.string.season_rewards_title),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = readableOnBackground(Color(0xFF81C784))
        )

        val rewards = listOf(
            stringResource(R.string.season_reward_1),
            stringResource(R.string.season_reward_5),
            stringResource(R.string.season_reward_10),
            stringResource(R.string.season_reward_15),
            stringResource(R.string.season_reward_20)
        )

        rewards.forEachIndexed { idx, reward ->
            val unlocked = data.level > idx * 5
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (unlocked) Color(0xFF1B5E20).copy(alpha = 0.2f) else Color(0xFF151D16),
                border = BorderStroke(
                    1.dp,
                    if (unlocked) Color(0xFF81C784) else Color(0xFF2E7D32).copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (unlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (unlocked) Color(0xFF81C784) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = reward,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = if (unlocked) Color.White else Color.Gray
                    )
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
