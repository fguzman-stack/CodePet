package com.tamagotchi.code.feature.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

import com.tamagotchi.code.ui.components.AnimatedPetSprite
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn

@Composable
fun LearnScreen(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Retos Normales", "Retos Especiales", "Insignias")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ">>> APRENDER",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (activeTab == 0) "Estudiando con ${state.name}" else "Desafíos de élite",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )
            }
            
            Box(modifier = Modifier.size(80.dp)) {
                AnimatedPetSprite(
                    status = state.currentStatus,
                    level = state.level,
                    equippedHat = state.equippedHat,
                    celebrationTrigger = viewModel.celebrationTrigger,
                    learningEventTrigger = viewModel.learningEventTrigger,
                    onClick = { viewModel.petThePet() },
                    modifier = Modifier.scale(0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (activeTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            0 -> QuizPanel(viewModel = viewModel)
            1 -> SpecialChallengesPanel(viewModel = viewModel, state = state)
            2 -> BadgesPanel(viewModel = viewModel)
        }
    }
}

@Composable
fun BadgesPanel(viewModel: PetViewModel) {
    val languageProgress by viewModel.languageProgressList.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> INSIGNIAS POR LENGUAJE",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Completa retos para desbloquear insignias: Bronce (50%), Plata (75%), Oro (100%)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        val languages = listOf("Kotlin", "JavaScript", "PHP", "Python")
        val languageKeyMap = mapOf("Kotlin" to "KOTLIN", "JavaScript" to "JS", "PHP" to "PHP", "Python" to "PYTHON")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            languages.forEach { langName ->
                val key = languageKeyMap[langName] ?: langName.uppercase()
                val progress = languageProgress.find { it.languageId == key }
                val total = progress?.totalChallenges ?: 10
                val completed = progress?.challengesCompleted ?: 0
                val badgeLevel = progress?.badgeLevel ?: 0
                val pct = if (total > 0) (completed.toFloat() / total * 100).toInt() else 0

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF151D16),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val badgeIcon = when {
                            badgeLevel >= 3 -> Icons.Default.EmojiEvents
                            badgeLevel >= 2 -> Icons.Default.WorkspacePremium
                            badgeLevel >= 1 -> Icons.Default.MilitaryTech
                            else -> Icons.Default.Code
                        }
                        val badgeColor = when {
                            badgeLevel >= 3 -> Color(0xFFFFD700)
                            badgeLevel >= 2 -> Color(0xFFC0C0C0)
                            badgeLevel >= 1 -> Color(0xFFCD7F32)
                            else -> Color.Gray
                        }

                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = "Badge $langName",
                            tint = badgeColor,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = langName,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(
                                text = "$completed/$total retos ($pct%)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { if (total > 0) completed.toFloat() / total else 0f },
                                color = badgeColor,
                                trackColor = Color(0xFF2E7D32).copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth().height(4.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = badgeLevel > 0,
                            enter = scaleIn() + fadeIn()
                        ) {
                            val badgeName = when (badgeLevel) {
                                3 -> "ORO"
                                2 -> "PLATA"
                                1 -> "BRONCE"
                                else -> ""
                            }
                            Text(
                                text = badgeName,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = badgeColor,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizPanel(viewModel: PetViewModel) {
    val challengesList by viewModel.activeChallenges.collectAsStateWithLifecycle()
    val currentIndex by viewModel.currentChallengeIndex.collectAsStateWithLifecycle()
    val feedback by viewModel.challengeFeedback.collectAsStateWithLifecycle()
    val currentLang by viewModel.selectedChallengeLanguage.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> DESAFÍO DE PROGRAMACIÓN: Arena $currentLang",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (challengesList.isEmpty()) {
            Text(
                text = "Cargando acertijos...",
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                fontSize = 12.sp
            )
        } else if (currentIndex < challengesList.size) {
            val challenge = challengesList[currentIndex]
            
            // Comentario dinámico de la mascota
            val petTip = remember(challenge.id) {
                when(challenge.type) {
                    "DEBUG" -> "¡Cuidado! Ese bug muerde. Revisa bien los puntos y coma."
                    "TRIVIA" -> "Esta es fácil... si has leído la documentación."
                    else -> "Concéntrate, mi CPU depende de tu respuesta."
                }
            }

            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
                color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color(0xFF81C784).copy(alpha = 0.3f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = petTip,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text(
                text = "Desafío ${currentIndex + 1} de ${challengesList.size} (${if (challenge.type == "DEBUG") "Desbuguear" else "Trivia"}):",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray
            )
            Text(
                text = challenge.title,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            challenge.codeSnippet?.let { code ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF151D16),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = code,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFA5D6A7),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Text(
                text = challenge.question,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                challenge.options.forEachIndexed { optIndex, optionText ->
                    val isCorrectSelection = feedback == "CORRECT" && optIndex == challenge.correctAnswerIndex
                    val optionBorderColor = if (feedback != null) {
                        if (optIndex == challenge.correctAnswerIndex) Color(0xFF81C784) else Color(0xFFEF5350).copy(alpha = 0.3f)
                    } else {
                        Color(0xFF2E7D32).copy(alpha = 0.6f)
                    }

                    val optionBgColor = if (feedback != null) {
                        if (optIndex == challenge.correctAnswerIndex) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFFB71C1C).copy(alpha = 0.05f)
                    } else {
                        Color(0xFF151D16)
                    }

                    Surface(
                        onClick = {
                            if (feedback == null) {
                                viewModel.submitAnswer(optIndex)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = optionBgColor,
                        border = BorderStroke(1.dp, optionBorderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("quiz_option_$optIndex")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "[$optIndex] ",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF81C784),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                            Text(
                                text = optionText,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            val fb = feedback
            if (fb != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (fb == "CORRECT") Color(0xFF1B5E20).copy(alpha = 0.2f) else Color(0xFFB71C1C).copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (fb == "CORRECT") Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = fb,
                                tint = if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (fb == "CORRECT") "¡ACERTADO! +Alimento +Bytes +XP" else "RESPUESTA INCORRECTA",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (fb == "CORRECT") Color(0xFF81C784) else Color(0xFFEF5350)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = challenge.explanation,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color.LightGray,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.nextChallenge() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("quiz_next_button")
                ) {
                    Text(
                        text = if (currentIndex + 1 < challengesList.size) "SIGUIENTE ACERTIJO" else "CARGAR MÁS RETOS",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SpecialChallengesPanel(viewModel: PetViewModel, state: PetStateEntity) {
    val unlockedThemes by viewModel.unlockedThemes.collectAsStateWithLifecycle()
    var currentChallenge by remember { mutableStateOf(com.tamagotchi.code.data.SpecialChallengesData.challenges.random()) }
    var showFeedback by remember { mutableStateOf<Boolean?>(null) }
    var unlockedThemeName by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RETOS ESPECIALES",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(Icons.Default.Star, contentDescription = "Especial", tint = MaterialTheme.colorScheme.primary)
        }

        Text(
            text = "Resuelve ejercicios avanzados de lógica y algoritmos para desbloquear nuevos temas visuales exclusivos.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = currentChallenge.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = currentChallenge.question,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Monospace
                )

                if (currentChallenge.codeSnippet != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentChallenge.codeSnippet!!,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                currentChallenge.options.forEachIndexed { index, optionText ->
                    val isCorrect = index == currentChallenge.correctAnswerIndex

                    val cardColor = if (!isAnswered) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else if (isCorrect) {
                        Color(0xFF2E7D32).copy(alpha = 0.5f)
                    } else {
                        Color(0xFFB71C1C).copy(alpha = 0.5f)
                    }

                    Card(
                        onClick = {
                            if (!isAnswered) {
                                isAnswered = true
                                if (index == currentChallenge.correctAnswerIndex) {
                                    showFeedback = true
                                    viewModel.soundManager.playLevelUp()
                                    viewModel.completeMinigame(bytesEarned = 50, healthEarned = 10f, energyCost = 0f)

                                    val lockedThemes = com.tamagotchi.code.ui.theme.ThemeRegistry.allThemes.map { it.name }.filter { !unlockedThemes.contains(it) }
                                    if (lockedThemes.isNotEmpty()) {
                                        val randomTheme = lockedThemes.random()
                                        unlockedThemeName = randomTheme
                                        viewModel.unlockTheme(randomTheme)
                                    } else {
                                        unlockedThemeName = "¡Ya tienes todos!"
                                    }
                                } else {
                                    showFeedback = false
                                    viewModel.soundManager.playClick()
                                }
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        border = BorderStroke(1.dp, if (!isAnswered) Color.Transparent else if (isCorrect) Color(0xFF4CAF50) else Color(0xFFEF5350)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = optionText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        if (isAnswered) {
            val message = if (showFeedback == true) "¡Respuesta Correcta!" else "Incorrecto."
            val color = if (showFeedback == true) Color(0xFF4CAF50) else Color(0xFFEF5350)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = message,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )

                Text(
                    text = currentChallenge.explanation,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                if (showFeedback == true && unlockedThemeName != null && unlockedThemeName != "¡Ya tienes todos!") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¡Nuevo tema desbloqueado: $unlockedThemeName!",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isAnswered = false
                        showFeedback = null
                        unlockedThemeName = null
                        currentChallenge = com.tamagotchi.code.data.SpecialChallengesData.challenges.random()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Siguiente Reto", color = MaterialTheme.colorScheme.onPrimary, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
