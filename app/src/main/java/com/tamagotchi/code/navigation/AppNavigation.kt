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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.feature.focus.FocusScreen
import com.tamagotchi.code.feature.home.HomeScreen
import com.tamagotchi.code.feature.learn.LearnScreen

import com.tamagotchi.code.feature.shop.ShopScreen
import com.tamagotchi.code.ui.theme.LocalAppTheme
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

    val appTheme = LocalAppTheme.current

    if (!viewModel.hasSeenOnboarding.value) {
        com.tamagotchi.code.feature.onboarding.OnboardingScreen(
            onComplete = { name, topics -> viewModel.completeOnboarding(name, topics) },
            onSkip = { viewModel.skipOnboarding() }
        )
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                actions = {
                    IconButton(
                        onClick = { activeScreen = Screen.Settings },
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
                        label = { Text(item.label, style = MaterialTheme.typography.labelSmall) }
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
                            activeScreen = Screen.Settings
                        },
                        onPlayClick = { activeScreen = Screen.Games }
                    )
                }
                Screen.Learn -> {
                    petState?.let { state ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
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
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
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
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            ShopScreen(viewModel = viewModel, state = state)
                        }
                    }
                }
                Screen.Settings -> {
                    com.tamagotchi.code.feature.settings.SettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Home },
                        onNavigateToLanguage = { activeScreen = Screen.SettingsLanguage }
                    )
                }
                Screen.SettingsLanguage -> {
                    com.tamagotchi.code.feature.settings.SettingsLanguageScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Settings }
                    )
                }
                Screen.Games -> {
                    com.tamagotchi.code.feature.games.GamesScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Home },
                        onNavigateToBugHunt = { activeScreen = Screen.BugHunt },
                        onNavigateToGitRescue = { activeScreen = Screen.GitRescue },
                        onNavigateToRefactorRush = { activeScreen = Screen.RefactorRush }
                    )
                }
                Screen.BugHunt -> {
                    com.tamagotchi.code.feature.games.BugHuntScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Games }
                    )
                }
                Screen.GitRescue -> {
                    com.tamagotchi.code.feature.games.GitRescueScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Games }
                    )
                }
                Screen.RefactorRush -> {
                    com.tamagotchi.code.feature.games.RefactorRushScreen(
                        viewModel = viewModel,
                        onNavigateBack = { activeScreen = Screen.Games }
                    )
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
}
