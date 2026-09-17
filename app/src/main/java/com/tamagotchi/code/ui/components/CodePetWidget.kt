package com.tamagotchi.code.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity

@Composable
fun CodePetWidget(
    petState: PetStateEntity?,
    modifier: Modifier = Modifier
) {
    val status = if (petState?.isDead == true) "DEAD" else petState?.currentStatus ?: "HAPPY"
    val petImageRes = when (status) {
        "DEAD" -> R.drawable.mascota_dead
        "SLEEPING" -> R.drawable.mascota_sleeping
        "STUDYING" -> R.drawable.mascota_studying
        "SICK" -> R.drawable.mascota_sick
        "SAD" -> R.drawable.mascota_sad
        "HUNGRY" -> R.drawable.mascota_hungry
        "EXCITED" -> R.drawable.mascota_excited
        else -> R.drawable.mascota_happy
    }

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF1A1C2E),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF25284D), Color(0xFF1A1C2E))
                    )
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header with name and hearts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = petState?.name ?: stringResource(R.string.default_pet_name),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.season_level_short, petState?.level ?: 1),
                            color = Color(0xFF7C8CFB),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Row {
                        repeat(3) { index ->
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = if (index < (petState?.health ?: 100f) / 33) Color(0xFFFF4B6B) else Color(0xFF3F4466),
                                modifier = Modifier.size(18.dp).padding(horizontal = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pet Image in a spotlight
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0x337C8CFB), Color.Transparent)
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = petImageRes),
                        contentDescription = stringResource(R.string.cd_widget_pet),
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats bars
                StatBar(label = stringResource(R.string.meter_energy), value = (petState?.energy ?: 100f) / 100f, color = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.height(8.dp))
                StatBar(label = stringResource(R.string.widget_hunger), value = (petState?.hunger ?: 100f) / 100f, color = Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun StatBar(label: String, value: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color(0xFFB7C2D9), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(text = "${(value * 100).toInt()}%", color = Color.White, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = value,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = Color(0xFF2D3142)
        )
    }
}
