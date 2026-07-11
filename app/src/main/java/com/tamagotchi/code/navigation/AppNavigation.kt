package com.tamagotchi.code.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.feature.focus.FocusScreen
import com.tamagotchi.code.feature.games.MinigamesDialog
import com.tamagotchi.code.feature.home.HomeScreen
import com.tamagotchi.code.feature.learn.LearnScreen
import com.tamagotchi.code.feature.settings.PersonalizeDialog
import com.tamagotchi.code.feature.shop.ShopScreen
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.viewmodel.PetViewModel

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

val bottomNavItems = listOf(
    BottomNavItem("Inicio", Icons.Default.Home, Screen.Home),
    BottomNavItem("Aprender", Icons.Default.Code, Screen.Learn),
    BottomNavItem("Estudio", Icons.Default.Timer, Screen.Focus),
    BottomNavItem("Tienda", Icons.Default.ShoppingBag, Screen.Shop)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: PetViewModel) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val studySessions by viewModel.studySessions.collectAsStateWithLifecycle()

    var activeScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameInput by remember { mutableStateOf("") }
    var showMinigamesDialog by remember { mutableStateOf(false) }

    val currentThemeValue = viewModel.currentTheme.value
    val appTheme = ThemeRegistry.getTheme(currentThemeValue)
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
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = activeScreen == item.screen,
                        onClick = { activeScreen = item.screen },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, fontFamily = FontFamily.Monospace, fontSize = 10.sp) }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeScreen) {
                Screen.Home -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onRenameClick = {
                            petState?.let { renameInput = it.name }
                            showRenameDialog = true
                        },
                        onPlayClick = { showMinigamesDialog = true }
                    )
                }
                Screen.Learn -> {
                    petState?.let { state ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC000000))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            LearnScreen(viewModel = viewModel, state = state)
                        }
                    }
                }
                Screen.Focus -> {
                    petState?.let { state ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC000000))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            FocusScreen(
                                viewModel = viewModel,
                                state = state,
                                studySessions = studySessions
                            )
                        }
                    }
                }
                Screen.Shop -> {
                    petState?.let { state ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xCC000000))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ShopScreen(viewModel = viewModel, state = state)
                        }
                    }
                }
            }

            if (petState == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
                }
            }
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
