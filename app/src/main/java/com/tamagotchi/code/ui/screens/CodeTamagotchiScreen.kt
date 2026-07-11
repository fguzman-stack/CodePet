package com.tamagotchi.code.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeTamagotchiScreen(
    viewModel: PetViewModel,
    modifier: Modifier = Modifier
) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val studySessions by viewModel.studySessions.collectAsStateWithLifecycle()

    var activePanel by remember { mutableStateOf("HOME") }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameInput by remember { mutableStateOf("") }
    var showMinigamesDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    
    val currentTheme = viewModel.currentTheme.value
    val appTheme = com.tamagotchi.code.ui.theme.ThemeRegistry.getTheme(currentTheme)
    val themeBackgroundColor = appTheme.background

    Scaffold(
        containerColor = themeBackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DeveloperMode,
                            contentDescription = "Code Tamagotchi Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Code Tamagotchi",
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                actions = {
                    IconButton(
                        onClick = { showRenameDialog = true },
                        modifier = Modifier.testTag("action_edit_pet")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurar Mascota",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                NavigationBarItem(
                    selected = activePanel == "HOME",
                    onClick = { activePanel = "HOME" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                )
                NavigationBarItem(
                    selected = activePanel == "LEARN",
                    onClick = { activePanel = "LEARN" },
                    icon = { Icon(Icons.Default.Code, contentDescription = "Aprender") },
                    label = { Text("Aprender", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                )
                NavigationBarItem(
                    selected = activePanel == "STUDY",
                    onClick = { activePanel = "STUDY" },
                    icon = { Icon(Icons.Default.Timer, contentDescription = "Estudio") },
                    label = { Text("Estudio", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                )
                NavigationBarItem(
                    selected = activePanel == "SHOP",
                    onClick = { activePanel = "SHOP" },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Tienda") },
                    label = { Text("Tienda", fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        petState?.let { state ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
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
                        onRenameClick = {
                            renameInput = state.name
                            showRenameDialog = true
                        },
                        onPlayClick = { showMinigamesDialog = true }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            AnimatedVisibility(
                visible = activePanel != "HOME",
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xCC000000))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    TerminalHub(
                        activePanel = activePanel,
                        state = state,
                        studySessions = studySessions,
                        viewModel = viewModel
                    )
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
        }
    }

    val unlockedThemes by viewModel.unlockedThemes.collectAsStateWithLifecycle()

    if (showRenameDialog) {
        petState?.let { state ->
            PersonalizeDialog(
                currentName = state.name,
                currentLanguage = state.language,
                currentTheme = viewModel.currentTheme.value,
                unlockedThemes = unlockedThemes,
                onDismiss = { showRenameDialog = false },
                onSave = { name, language, theme ->
                    viewModel.renamePet(name)
                    viewModel.selectLanguage(language)
                    viewModel.changeTheme(theme)
                    showRenameDialog = false
                }
            )
        }
    }

    if (showMinigamesDialog) {
        petState?.let { state ->
            MinigamesDialog(
                state = state,
                viewModel = viewModel,
                onDismiss = { showMinigamesDialog = false }
            )
        }
    }
}

@Composable
fun ViewportCard(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onRenameClick: () -> Unit,
    onPlayClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val bounceScale = remember { Animatable(1f) }
    val bounceOffsetY = remember { Animatable(0f) }
    val heartOffsetY = remember { Animatable(0f) }
    val heartAlpha = remember { Animatable(0f) }
    var showHeart by remember { mutableStateOf(false) }

    fun onPetTap() {
        viewModel.petThePet()
        viewModel.soundManager.playClick()
        scope.launch {
            showHeart = true
            bounceScale.snapTo(1f)
            bounceOffsetY.snapTo(0f)
            heartOffsetY.snapTo(0f)
            heartAlpha.snapTo(1f)
            launch { bounceScale.animateTo(1.25f, tween(100)); bounceScale.animateTo(1f, spring(dampingRatio = 0.3f)) }
            launch { bounceOffsetY.animateTo(-20f, tween(100)); bounceOffsetY.animateTo(0f, spring(dampingRatio = 0.3f)) }
            launch { heartOffsetY.animateTo(-120f, tween(800)); heartAlpha.animateTo(0f, tween(800)) }
            delay(900)
            showHeart = false
        }
    }

    val petImageRes = when (state.currentStatus) {
        "SLEEPING" -> R.drawable.mascota_sleeping
        "STUDYING" -> R.drawable.mascota_studying
        "SICK" -> R.drawable.mascota_sick
        "SAD" -> R.drawable.mascota_sad
        "HUNGRY" -> R.drawable.mascota_hungry
        "EXCITED" -> R.drawable.mascota_excited
        else -> R.drawable.mascota_happy
    }

    val statusColor = when (state.currentStatus) {
        "SLEEPING" -> Color(0xFF64B5F6)
        "STUDYING" -> Color(0xFF81C784)
        "SICK" -> Color(0xFFE57373)
        "SAD" -> Color(0xFF90A4AE)
        "HUNGRY" -> Color(0xFFFFB74D)
        "EXCITED" -> Color(0xFFFF80AB)
        else -> Color(0xFFBA68C8)
    }

    val randomQuote = remember(state.currentStatus) {
        val quotes = when (state.currentStatus) {
            "SLEEPING" -> listOf(
                "Zzz... if (dream) { sleep() } else { repeat() }... Zzz",
                "Cargando baterías... no interrumpas mi hilo principal.",
                "Soñando con compiladores veloces y cero NullPointers..."
            )
            "STUDYING" -> listOf(
                "¡Shhh! Estoy optimizando algoritmos en mi cerebro.",
                "Compilando... codeando a 1000 WPM.",
                "Siento cómo se incrementa mi sinapsis neuronal binaria."
            )
            "SICK" -> listOf(
                "Error 500: Necesito desbuguear urgente. ¡Dame una píldora!",
                "Demasiados bugs acumulados en mi stack... me siento mal.",
                "Siento mi CPU sobrecalentada. ¿Podemos repasar un poco?"
            )
            "SAD" -> listOf(
                "Tengo flojera... me siento un poco depre.",
                "Mi batería de motivación está por debajo del 20%.",
                "¿Procrastinando otra vez? Mi código se llena de advertencias."
            )
            "HUNGRY" -> listOf(
                "¡NullPointerException en mi estómago! Necesito bytes.",
                "Mi caché de energía está vacía, ¿me das de comer?",
                "Sin comida, mi rendimiento cae a O(n^2)."
            )
            "EXCITED" -> listOf(
                "¡Wiii! ¡Mi código es O(1) y mi corazón también!",
                "¡Nivel de felicidad al MÁXIMO! Gracias por quererme.",
                "¡Siento que podría compilar el kernel de Linux en 1 segundo!"
            )
            else -> listOf(
                "¡Compilar sin advertencias es mi pasión!",
                "¿Listo para tirar unas líneas de código limpias hoy?",
                "¡Siento el poder de un refactor exitoso!",
                "Me agradas, haces que mi arquitectura sea modular y sólida."
            )
        }
        quotes.random()
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, statusColor.copy(alpha = 0.8f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("pet_viewport_card")
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
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.testTag("pet_name_text")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Renombrar",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onRenameClick() }
                        )
                    }
                    Text(
                        text = "Especialista: ${state.language}",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentHearts = (state.health / 20f).toInt().coerceIn(0, 5)
                        for (i in 1..5) {
                            val isFilled = i <= currentHearts
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Corazón $i",
                                tint = if (isFilled) Color(0xFFEF5350) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, statusColor),
                    modifier = Modifier.testTag("level_badge")
                ) {
                    Text(
                        text = "LVL ${state.level}",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val infiniteTransition = rememberInfiniteTransition(label = "pet_animation")
            
            val offsetY by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "EXCITED" || state.currentStatus == "HAPPY") -10f else if (state.currentStatus == "SLEEPING") -5f else 0f,
                targetValue = if (state.currentStatus == "EXCITED" || state.currentStatus == "HAPPY") 10f else if (state.currentStatus == "SLEEPING") 5f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "EXCITED") 300 else if (state.currentStatus == "HAPPY") 600 else if (state.currentStatus == "SLEEPING") 2000 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_offset_y"
            )

            val offsetX by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "SICK") -5f else 0f,
                targetValue = if (state.currentStatus == "SICK") 5f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "SICK") 100 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_offset_x"
            )
            
            val scale by infiniteTransition.animateFloat(
                initialValue = if (state.currentStatus == "HUNGRY") 0.95f else 1f,
                targetValue = if (state.currentStatus == "HUNGRY") 1.05f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(if (state.currentStatus == "HUNGRY") 1000 else 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pet_scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp, y = offsetY.dp)
                        .scale(bounceScale.value * scale)
                ) {
                        Image(
                            painter = painterResource(id = petImageRes),
                            contentDescription = "Estado: ${state.currentStatus}",
                            modifier = Modifier
                                .offset(y = bounceOffsetY.value.dp)
                                .size(170.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onPetTap() },
                            contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(statusColor, RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            text = state.currentStatus,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (showHeart) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Amor",
                            tint = Color(0xFFEF5350),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = heartOffsetY.value.dp)
                                .graphicsLayer(alpha = heartAlpha.value)
                                .size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Mensaje",
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = randomQuote,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val currentLevelRequiredXp = state.level * 100
            val xpProgress = (state.xp.toFloat() / currentLevelRequiredXp.toFloat()).coerceIn(0f, 100f)
            val animatedXpProgress by animateFloatAsState(targetValue = xpProgress)

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "XP: ${state.xp} / $currentLevelRequiredXp",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(xpProgress * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { animatedXpProgress },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MeterItem(
                    label = "Vida", value = state.health,
                    icon = Icons.Default.Favorite, activeColor = Color(0xFFEF5350),
                    trackColor = Color(0xFFEF5350).copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("health_bar")
                )
                MeterItem(
                    label = "Alimento", value = state.hunger,
                    icon = Icons.Default.Restaurant, activeColor = Color(0xFFFFA726),
                    trackColor = Color(0xFFFFA726).copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("hunger_bar")
                )
                MeterItem(
                    label = "Energía", value = state.energy,
                    icon = Icons.Default.FlashOn, activeColor = Color(0xFF29B6F6),
                    trackColor = Color(0xFF29B6F6).copy(alpha = 0.2f),
                    modifier = Modifier.weight(1f).testTag("energy_bar")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.bytes} B", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color(0xFFFFD54F))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF7043), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${state.streak} días", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 13.sp, color = Color(0xFFFF7043))
                }
                Button(
                    onClick = { viewModel.toggleSleep() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (state.currentStatus == "SLEEPING") MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("action_toggle_sleep")
                ) {
                    Icon(
                        imageVector = if (state.currentStatus == "SLEEPING") Icons.Default.WbSunny else Icons.Default.NightsStay,
                        contentDescription = "Dormir/Despertar",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (state.currentStatus == "SLEEPING") "Despertar" else "Dormir",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = statusColor.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { viewModel.petThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.Pets, contentDescription = "Acariciar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Acariciar", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Button(
                    onClick = { viewModel.cleanThePet(); viewModel.soundManager.playClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = "Limpiar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Limpiar", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Button(
                    onClick = { onRenameClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Ajustar", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Ajustar", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Icon(Icons.Default.SportsEsports, contentDescription = "Jugar", tint = Color.Black, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Jugar", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

@Composable
fun StatusMetersSection(state: PetStateEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MeterItem(
                label = "Vida",
                value = state.health,
                icon = Icons.Default.Favorite,
                activeColor = Color(0xFFEF5350),
                trackColor = Color(0xFFEF5350).copy(alpha = 0.2f),
                modifier = Modifier
                    .weight(1f)
                    .testTag("health_bar")
            )
            MeterItem(
                label = "Alimento",
                value = state.hunger,
                icon = Icons.Default.Restaurant,
                activeColor = Color(0xFFFFA726),
                trackColor = Color(0xFFFFA726).copy(alpha = 0.2f),
                modifier = Modifier
                    .weight(1f)
                    .testTag("hunger_bar")
            )
            MeterItem(
                label = "Energía",
                value = state.energy,
                icon = Icons.Default.FlashOn,
                activeColor = Color(0xFF29B6F6),
                trackColor = Color(0xFF29B6F6).copy(alpha = 0.2f),
                modifier = Modifier
                    .weight(1f)
                    .testTag("energy_bar")
            )
        }
    }
}

@Composable
fun MeterItem(
    label: String,
    value: Float,
    icon: ImageVector,
    activeColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(targetValue = value / 100f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = activeColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            color = activeColor,
            trackColor = trackColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${value.toInt()}%",
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BalanceAndQuickActionsRow(
    state: PetStateEntity,
    viewModel: PetViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Code Coins / Bytes",
                    tint = Color(0xFFFFD54F)
                )
                Column {
                    Text(
                        text = "Bytes",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${state.bytes} B",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp,
                        color = Color(0xFFFFD54F)
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFFF7043).copy(alpha = 0.5f)),
            color = Color(0xFFFF7043).copy(alpha = 0.05f),
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Racha de estudio",
                    tint = Color(0xFFFF7043)
                )
                Column {
                    Text(
                        text = "Racha",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${state.streak} DÍAS",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = Color(0xFFFF7043)
                    )
                }
            }
        }

        Button(
            onClick = { viewModel.toggleSleep() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (state.currentStatus == "SLEEPING") {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                contentColor = if (state.currentStatus == "SLEEPING") {
                    MaterialTheme.colorScheme.onSecondary
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1.2f)
                .height(48.dp)
                .testTag("action_toggle_sleep")
        ) {
            Icon(
                imageVector = if (state.currentStatus == "SLEEPING") Icons.Default.WbSunny else Icons.Default.NightsStay,
                contentDescription = "Dormir / Despertar",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (state.currentStatus == "SLEEPING") "Despertar" else "Dormir",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TerminalHub(
    activePanel: String,
    state: PetStateEntity,
    studySessions: List<StudySessionEntity>,
    viewModel: PetViewModel
) {
    val currentTheme = viewModel.currentTheme.value
    val appTheme = com.tamagotchi.code.ui.theme.ThemeRegistry.getTheme(currentTheme)
    val themeColor = appTheme.primary
    val themeTextColor = appTheme.secondary

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, themeColor),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xCC000000)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .testTag("terminal_hub_container")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFEF5350)))
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFFFB74D)))
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF81C784)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "terminal@${state.name.lowercase()}:~",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = themeTextColor
                    )
                }

                Text(
                    text = "v1.0.0",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF4CAF50).copy(alpha = 0.6f)
                )
            }

            HorizontalDivider(
                color = Color(0xFF2E7D32).copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 10.dp)
            )

            when (activePanel) {
                "STUDY" -> StudyHub(viewModel = viewModel, state = state, studySessions = studySessions)
                "LEARN" -> LearnHub(viewModel = viewModel, state = state)
                "SHOP" -> ShopPanel(viewModel = viewModel, state = state)
            }
        }
    }
}

@Composable
fun StudyHub(viewModel: PetViewModel, state: PetStateEntity, studySessions: List<StudySessionEntity>) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Temporizador", "Bitácora")

    Column(modifier = Modifier.fillMaxWidth()) {
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
            0 -> TimerPanel(viewModel = viewModel, state = state)
            1 -> LogsPanel(studySessions = studySessions, state = state, viewModel = viewModel)
        }
    }
}

@Composable
fun LearnHub(viewModel: PetViewModel, state: PetStateEntity) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Retos Normales", "Retos Especiales")

    Column(modifier = Modifier.fillMaxWidth()) {
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
        }
    }
}

@Composable
fun TimerPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val isRunning = viewModel.isTimerRunning.value
    val secondsRemaining = viewModel.timerSecondsRemaining.value
    val selectedMinutes = viewModel.timerSelectedMinutes.value
    val currentTopic = viewModel.currentStudyTopic.value

    var tempMinutes by remember { mutableIntStateOf(25) }
    var tempTopic by remember { mutableStateOf("Kotlin") }
    var showDropdown by remember { mutableStateOf(false) }

    val topics = listOf("Kotlin", "JavaScript", "PHP", "Python", "SQL", "Clean Code", "Git", "Estructuras de Datos")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isRunning) {
            Text(
                text = ">>> INICIAR BITÁCORA DE ESTUDIO",
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF81C784),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Materia:",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.width(80.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    Surface(
                        onClick = { showDropdown = true },
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tempTopic,
                                color = Color(0xFF81C784),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Cambiar",
                                tint = Color(0xFF81C784)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.background(Color(0xFF151D16))
                    ) {
                        topics.forEach { topic ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = topic,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    tempTopic = topic
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiempo:",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.width(80.dp)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 25, 50).forEach { mins ->
                        val isSelected = tempMinutes == mins
                        Surface(
                            onClick = { tempMinutes = mins },
                            color = if (isSelected) Color(0xFF2E7D32) else Color(0xFF151D16),
                            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "$mins min",
                                    color = if (isSelected) Color.Black else Color(0xFF81C784),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Al estudiar: El Tamagotchi entrará en modo concentrado. Completar la sesión te recompensa con Bytes y XP de estudio, pero drenará energía mental.",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (state.currentStatus == "SLEEPING") {
                        viewModel.toggleSleep()
                    }
                    viewModel.startStudyTimer(tempMinutes, tempTopic)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("timer_start_button")
            ) {
                Text(
                    text = "EMPEZAR COMPILACIÓN",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            Text(
                text = ">>> MODO: COMPILANDO HORAS DE ESTUDIO",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFB74D),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Enfoque: $currentTopic",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minText = String.format("%02d", secondsRemaining / 60)
            val secText = String.format("%02d", secondsRemaining % 60)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$minText:$secText",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF81C784),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Mascota: ${state.name} está estudiando contigo...",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.cancelStudyTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("timer_cancel_button")
                ) {
                    Text(
                        text = "CANCELAR COMPILACIÓN",
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
fun QuizPanel(viewModel: PetViewModel) {
    val challengesList by viewModel.activeChallenges.collectAsStateWithLifecycle()
    val index by viewModel.currentChallengeIndex.collectAsStateWithLifecycle()
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
        } else if (index < challengesList.size) {
            val challenge = challengesList[index]

            Text(
                text = "Desafío ${index + 1} de ${challengesList.size} (${if (challenge.type == "DEBUG") "Desbuguear" else "Trivia"}):",
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

            feedback?.let { fb ->
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
                        text = if (index + 1 < challengesList.size) "SIGUIENTE ACERTIJO" else "CARGAR MÁS RETOS",
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
fun ShopPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val shopItems = listOf(
        ShopItemData("Café Negro (CPU Booster)", 10, "Restaura +20 Energía mental", 0f, 0f, 20f, Icons.Default.Coffee),
        ShopItemData("Pizza de Código (Bytes Snack)", 15, "Restaura +35 Alimento", 35f, 0f, 0f, Icons.Default.LocalPizza),
        ShopItemData("Píldora Desbugueadora", 25, "Cura de infecciones y sana +30 Salud", 0f, 30f, 0f, Icons.Default.Medication),
        ShopItemData("Vacuna Super Compiler", 55, "Restaura +75 Salud, +40 Alimento, +40 Energía", 40f, 75f, 40f, Icons.Default.Shield)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> TIENDA DE RECURSOS PARA MASCOTAS",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Compra raciones o medicinas con tus Bytes de estudio acumulados.",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shopItems.forEach { item ->
                val canAfford = state.bytes >= item.cost
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF151D16),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (canAfford) Color(0xFFFFD54F) else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(
                                text = item.effect,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.buyShopItem(
                                    itemName = item.name,
                                    cost = item.cost,
                                    hungerRestore = item.hungerRestore,
                                    healthRestore = item.healthRestore,
                                    energyRestore = item.energyRestore
                                )
                            },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD54F),
                                disabledContainerColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("shop_buy_${item.name.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = "${item.cost} B",
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
    }
}

data class ShopItemData(
    val name: String,
    val cost: Int,
    val effect: String,
    val hungerRestore: Float,
    val healthRestore: Float,
    val energyRestore: Float,
    val icon: ImageVector
)

@Composable
fun LogsPanel(
    studySessions: List<StudySessionEntity>,
    state: PetStateEntity,
    viewModel: PetViewModel
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> BITÁCORA DE COMPILACIÓN (ESTUDIOS)",
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (studySessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "[Sin registros de estudio aún]\n¡Empieza un temporizador para compilar tus primeras horas!",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
            ) {
                items(studySessions) { session ->
                    val dateStr = dateFormat.format(Date(session.timestamp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estudio: ${session.topic}",
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = dateStr,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = "+${session.durationMinutes} Min",
                                color = Color(0xFF81C784),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E281F),
            border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "[!] NOTA DEL COMPILADOR DE VIDA:",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFB74D),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Si dejas de registrar sesiones por más de 36 horas, tu racha de estudio se reiniciará a 0, y tu mascota perderá salud por falta de mantenimiento.",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.LightGray,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizeDialog(
    currentName: String,
    currentLanguage: String,
    currentTheme: String,
    unlockedThemes: Set<String>,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var nameInput by remember { mutableStateOf(currentName) }
    var selectedLang by remember { mutableStateOf(currentLanguage) }
    var selectedTheme by remember { mutableStateOf(currentTheme) }
    
    val languages = listOf("Kotlin", "Java", "Python", "JavaScript", "C++", "C#", "Go", "Rust", "Ruby", "Swift", "TypeScript", "PHP", "Dart", "HTML/CSS", "SQL", "Shell", "Haskell", "Lua", "Scala", "R", "Perl")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("personalize_dialog")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Ajustar Mi Mascota",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre de Mascota", fontFamily = FontFamily.Monospace) },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_name_input")
                )

                Text(
                    text = "Lenguaje de Especialidad:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    languages.forEach { lang ->
                        val isSelected = selectedLang.equals(lang, ignoreCase = true)
                        Surface(
                            onClick = { selectedLang = lang },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = lang,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Tema Visual:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    com.tamagotchi.code.ui.theme.ThemeRegistry.allThemes.forEach { themeConfig ->
                        val themeName = themeConfig.name
                        val isUnlocked = unlockedThemes.contains(themeName)
                        val isSelected = selectedTheme == themeName
                        Surface(
                            onClick = { if (isUnlocked) selectedTheme = themeName },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else if (isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(themeConfig.background, RoundedCornerShape(4.dp))
                                            .border(1.dp, themeConfig.primary, RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = themeName,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Seleccionado",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else if (!isUnlocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Bloqueado",
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = {
                            if (nameInput.isNotBlank()) {
                                onSave(nameInput, selectedLang, selectedTheme)
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_save_button")
                    ) {
                        Text("Guardar", fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Composable
fun CareCenterSection(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onPlayClick: () -> Unit
) {
    var showFoodMenu by remember { mutableStateOf(false) }
    var actionFeedback by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(actionFeedback) {
        if (actionFeedback != null) {
            kotlinx.coroutines.delay(3000)
            actionFeedback = null
        }
    }

    val currentTheme = viewModel.currentTheme.value
    val appTheme = com.tamagotchi.code.ui.theme.ThemeRegistry.getTheme(currentTheme)
    val themeColor = appTheme.primary

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)
        ),
        border = BorderStroke(1.dp, themeColor.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = ">>> CENTRO DE CUIDADOS",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = actionFeedback != null) {
                actionFeedback?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = themeColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, themeColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = msg,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.petThePet()
                        actionFeedback = "¡Mimos dados! A ${state.name} le encantan tus mimos (+10% Energía)."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Pets, contentDescription = "Acariciar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Acariciar", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Button(
                    onClick = {
                        viewModel.cleanThePet()
                        actionFeedback = "¡Limpieza completada! ${state.name} se siente brillante y sin bugs (+6% Salud, +5% Energía)."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CleaningServices, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Limpiar", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showFoodMenu = !showFoodMenu },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Restaurant, contentDescription = "Alimentar", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (showFoodMenu) "Ocultar comida" else "Alimentar", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD54F)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.SportsEsports, contentDescription = "Minijuegos", tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Minijuegos", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            AnimatedVisibility(visible = showFoodMenu) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Selecciona comida para ${state.name}:",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.LightGray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    val foods = listOf(
                        Triple("Manzana Binaria", 2, Triple(15f, 0f, 2f)),
                        Triple("Pizza de Bytes", 6, Triple(30f, 0f, 5f)),
                        Triple("Sushi de Datos", 15, Triple(55f, 5f, 15f)),
                        Triple("Café Espresso CPU", 5, Triple(-5f, 0f, 35f))
                    )

                    foods.forEach { food ->
                        val canAfford = state.bytes >= food.second
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = food.first,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                                Text(
                                    text = "Efecto: ${if (food.third.first > 0) "+${food.third.first.toInt()}% Alim " else ""}${if (food.third.third > 0) "+${food.third.third.toInt()}% Ener " else ""}${if (food.third.second > 0) "+${food.third.second.toInt()}% Vida" else ""}",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Gray
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.buyShopItem(
                                        itemName = food.first,
                                        cost = food.second,
                                        hungerRestore = food.third.first,
                                        healthRestore = food.third.second,
                                        energyRestore = food.third.third
                                    )
                                    actionFeedback = "¡Alimentado! Le diste ${food.first} a ${state.name}."
                                    showFoodMenu = false
                                },
                                enabled = canAfford,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFD54F),
                                    disabledContainerColor = Color.DarkGray
                                ),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text("${food.second} B", color = Color.Black, fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

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
                        text = if (selectedGame == null) ">>> MINI-JUEGOS" else ">>> DETALLE DE JUEGO",
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
                            imageVector = if (selectedGame != null) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Close,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                when (selectedGame) {
                    null -> {
                        Text(
                            text = "Juega con ${state.name} para aumentar su felicidad y conseguir Bytes extra.",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.LightGray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameOptionCard(
                            title = "Adivina el Bit",
                            description = "Intenta predecir el siguiente bit binario (0 o 1). Juego rápido de 5 rondas.",
                            icon = Icons.Default.Code,
                            onClick = { selectedGame = "BINARY_GUESS" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = "Caza de Bugs",
                            description = "¡Rápido! Los bugs se están escapando. Atrapa todos los que puedas en 10 segundos.",
                            icon = Icons.Default.BugReport,
                            onClick = { selectedGame = "BUG_SMASHER" }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameOptionCard(
                            title = "Servidor, Script, Hacker",
                            description = "Piedra, Papel o Tijera versión informática. ¡Derrota al compilador!",
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

@Composable
fun BinaryGuessGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var round by remember { mutableStateOf(1) }
    var score by remember { mutableStateOf(0) }
    var currentSecretBit by remember { mutableStateOf((0..1).random()) }
    var feedbackMessage by remember { mutableStateOf("¿Cuál crees que es el bit secreto?") }
    var showNextButton by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADIVINA EL BIT (Ronda $round de 5)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Black, RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFF2E7D32), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGameOver) "FIN" else if (showNextButton) "$currentSecretBit" else "?",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF81C784)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = feedbackMessage,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isGameOver) {
            val bytesReward = score * 4
            val healthReward = score * 3f
            Text(
                text = "¡Juego Terminado!\nAcertaste: $score de 5\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 10f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else if (showNextButton) {
            Button(
                onClick = {
                    if (round < 5) {
                        round += 1
                        currentSecretBit = (0..1).random()
                        feedbackMessage = "¿Cuál crees que es el bit secreto?"
                        showNextButton = false
                    } else {
                        isGameOver = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Siguiente Ronda", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val isCorrect = currentSecretBit == 0
                        if (isCorrect) {
                            score += 1
                            feedbackMessage = "¡Excelente! El bit secreto era 0."
                        } else {
                            feedbackMessage = "Incorrecto. El bit secreto era 1."
                        }
                        showNextButton = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                }

                Button(
                    onClick = {
                        val isCorrect = currentSecretBit == 1
                        if (isCorrect) {
                            score += 1
                            feedbackMessage = "¡Excelente! El bit secreto era 1."
                        } else {
                            feedbackMessage = "Incorrecto. El bit secreto era 0."
                        }
                        showNextButton = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("1", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                }
            }
        }
    }
}

@Composable
fun BugSmasherGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var score by remember { mutableStateOf(0) }
    var timeRemaining by remember { mutableStateOf(10) }
    var bugPosition by remember { mutableStateOf((0..8).random()) }
    var isStarted by remember { mutableStateOf(false) }

    LaunchedEffect(isStarted, timeRemaining) {
        if (isStarted && timeRemaining > 0) {
            kotlinx.coroutines.delay(1000)
            timeRemaining -= 1
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CAZA DE BUGS",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (!isStarted) {
            Text(
                text = "Toca los bugs que aparecen en la cuadrícula de 3x3 tan rápido como puedas. ¡Tienes 10 segundos!",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { isStarted = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¡Comenzar!", fontFamily = FontFamily.Monospace)
            }
        } else if (timeRemaining <= 0) {
            val bytesReward = score * 2
            val healthReward = (score * 1.5f).coerceAtMost(30f)
            Text(
                text = "¡Tiempo Agotado!\nBugs atrapados: $score\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 15f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bugs: $score",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tiempo: ${timeRemaining}s",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF5350)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            val isBug = bugPosition == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isBug) Color(0xFFFFCDD2) else Color(0xFF151D16))
                                    .border(1.dp, if (isBug) Color(0xFFEF5350) else Color(0xFF2E7D32).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .clickable {
                                        if (isBug) {
                                            score += 1
                                            bugPosition = (0..8).filter { it != index }.random()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isBug) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = "BUG",
                                        tint = Color(0xFFEF5350),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RockPaperSciGame(
    state: PetStateEntity,
    viewModel: PetViewModel,
    onFinish: () -> Unit
) {
    var userWins by remember { mutableStateOf(0) }
    var cpuWins by remember { mutableStateOf(0) }
    var roundMessage by remember { mutableStateOf("Elige tu jugada para iniciar la ronda.") }
    var userChoice by remember { mutableStateOf<String?>(null) }
    var cpuChoice by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    val choices = listOf("Servidor", "Script", "Hacker")
    val icons = mapOf(
        "Servidor" to Icons.Default.Computer,
        "Script" to Icons.Default.Description,
        "Hacker" to Icons.Default.BugReport
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SERVIDOR, SCRIPT, HACKER (RPS)",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "TÚ: $userWins | CPU: $cpuWins (Mejor de 3)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF2E7D32), RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TÚ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[userChoice] ?: Icons.AutoMirrored.Filled.Help,
                    contentDescription = userChoice ?: "Pregunta",
                    tint = if (userChoice != null) Color(0xFF81C784) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(userChoice ?: "Selecciona...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }

            Text("VS", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = Color(0xFFEF5350))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("COMPILADOR", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = icons[cpuChoice] ?: Icons.AutoMirrored.Filled.Help,
                    contentDescription = cpuChoice ?: "Pregunta",
                    tint = if (cpuChoice != null) Color(0xFFEF5350) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(cpuChoice ?: "Esperando...", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = roundMessage,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.LightGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (isGameOver) {
            val playerWon = userWins >= 2
            val bytesReward = if (playerWon) 20 else 5
            val healthReward = if (playerWon) 25f else 10f
            Text(
                text = if (playerWon) "¡Felicidades! Derrotaste al compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad" else "Has perdido contra el compilador.\nRecompensa: +$bytesReward Bytes, +${healthReward.toInt()}% Felicidad",
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFFFD54F),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.completeMinigame(bytesReward, healthReward, 10f)
                    onFinish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cobrar Recompensas", fontFamily = FontFamily.Monospace)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                choices.forEach { choice ->
                    Button(
                        onClick = {
                            userChoice = choice
                            val selectedCpu = choices.random()
                            cpuChoice = selectedCpu

                            if (choice == selectedCpu) {
                                roundMessage = "Empate en esta ronda con $choice."
                            } else if (
                                (choice == "Servidor" && selectedCpu == "Hacker") ||
                                (choice == "Script" && selectedCpu == "Servidor") ||
                                (choice == "Hacker" && selectedCpu == "Script")
                            ) {
                                userWins += 1
                                roundMessage = "¡Ganaste la ronda! $choice vence a $selectedCpu."
                            } else {
                                cpuWins += 1
                                roundMessage = "Perdiste la ronda. $selectedCpu vence a $choice."
                            }

                            if (userWins >= 2 || cpuWins >= 2) {
                                isGameOver = true
                                roundMessage = if (userWins >= 2) "¡Has ganado la partida!" else "El compilador ha ganado la partida."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151D16)),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) {
                        Text(choice, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF81C784))
                    }
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
                                    viewModel.completeMinigame(bytesEarned = 50, happinessBoost = 10f, energyBoost = 0f)
                                    
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
                        text = "🎁 ¡Nuevo tema desbloqueado: $unlockedThemeName!",
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
