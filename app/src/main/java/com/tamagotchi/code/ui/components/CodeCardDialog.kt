package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.CodeCard
import com.tamagotchi.code.data.CardType

@Composable
fun CodeCardDialog(
    card: CodeCard,
    onDismiss: () -> Unit
) {
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(600)
    )
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        flipped = true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        when (card.type) {
                            CardType.FACT -> R.string.cc_title_fact
                            CardType.JOKE -> R.string.cc_title_joke
                            CardType.TIP -> R.string.cc_title_tip
                        }
                    ),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(
                            rotationY = rotation,
                            cameraDistance = 12f * density.density
                        )
                        .then(
                            if (rotation > 90f) Modifier.rotate(180f) else Modifier
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = when (card.type) {
                        CardType.FACT -> Color(0xFF1A237E).copy(alpha = 0.1f)
                        CardType.JOKE -> Color(0xFFE65100).copy(alpha = 0.1f)
                        CardType.TIP -> Color(0xFF1B5E20).copy(alpha = 0.1f)
                    },
                    border = null
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = when (card.type) {
                                CardType.FACT -> Icons.Default.Lightbulb
                                CardType.JOKE -> Icons.Default.SentimentSatisfied
                                CardType.TIP -> Icons.Default.TipsAndUpdates
                            },
                            contentDescription = null,
                            tint = when (card.type) {
                                CardType.FACT -> Color(0xFF5C6BC0)
                                CardType.JOKE -> Color(0xFFFF7043)
                                CardType.TIP -> Color(0xFF66BB6A)
                            },
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = card.text,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.cc_another), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        }
    )
}
