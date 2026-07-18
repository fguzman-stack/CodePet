package com.tamagotchi.code.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tamagotchi.code.feature.focus.FocusScreen
import com.tamagotchi.code.feature.home.HomeScreen
import com.tamagotchi.code.feature.learn.LearnScreen
import com.tamagotchi.code.feature.shop.ShopScreen
import com.tamagotchi.code.feature.games.CodeReviewScreen
import com.tamagotchi.code.feature.games.HackathonScreen
import com.tamagotchi.code.feature.skills.SkillTreeScreen
import com.tamagotchi.code.feature.skills.SeasonPassScreen
import com.tamagotchi.code.feature.skills.WeeklyMissionsScreen
import com.tamagotchi.code.feature.settings.GitHubSyncScreen
import com.tamagotchi.code.ui.viewmodel.PetViewModel

object Routes {
    const val HOME = "home"
    const val LEARN = "learn"
    const val FOCUS = "focus"
    const val SHOP = "shop"
    const val SETTINGS = "settings"
    const val SETTINGS_LANGUAGE = "settings_language"
    const val SETTINGS_ABOUT = "settings_about"
    const val SETTINGS_GITHUB = "settings_github"
    const val GAMES = "games"
    const val BUG_HUNT = "bug_hunt"
    const val GIT_RESCUE = "git_rescue"
    const val REFACTOR_RUSH = "refactor_rush"
    const val CODE_REVIEW = "code_review"
    const val HACKATHON = "hackathon"
    const val SKILL_TREE = "skill_tree"
    const val SEASON_PASS = "season_pass"
    const val WEEKLY_MISSIONS = "weekly_missions"
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Inicio", Icons.Default.Home, Routes.HOME),
    BottomNavItem("Aprender", Icons.Default.Code, Routes.LEARN),
    BottomNavItem("Estudio", Icons.Default.Timer, Routes.FOCUS),
    BottomNavItem("Tienda", Icons.Default.ShoppingBag, Routes.SHOP)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: PetViewModel) {
    if (!viewModel.hasSeenOnboarding.value) {
        com.tamagotchi.code.feature.onboarding.OnboardingScreen(
            onComplete = { name, topics -> viewModel.completeOnboarding(name, topics) }
        )
        return
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.LEARN, Routes.FOCUS, Routes.SHOP)
    val appTheme = com.tamagotchi.code.ui.theme.LocalAppTheme.current
    val reduceMotion by viewModel.reduceMotion.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        com.tamagotchi.code.ui.components.AnimatedThemeBackground(
            theme = appTheme,
            reduceMotion = reduceMotion
        )

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            if (currentRoute in listOf(Routes.HOME, Routes.LEARN, Routes.FOCUS, Routes.SHOP)) {
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
                            onClick = { navController.navigate(Routes.SETTINGS) },
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
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    onRenameClick = { navController.navigate(Routes.SETTINGS) },
                    onPlayClick = { navController.navigate(Routes.GAMES) }
                )
            }
            composable(Routes.LEARN) {
                val petState by viewModel.petState.collectAsStateWithLifecycle()
                petState?.let { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        LearnScreen(viewModel = viewModel, state = state)
                    }
                }
            }
            composable(Routes.FOCUS) {
                val petState by viewModel.petState.collectAsStateWithLifecycle()
                val studySessions by viewModel.studySessions.collectAsStateWithLifecycle()
                petState?.let { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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
            composable(Routes.SHOP) {
                val petState by viewModel.petState.collectAsStateWithLifecycle()
                petState?.let { state ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        ShopScreen(viewModel = viewModel, state = state)
                    }
                }
            }
            composable(Routes.SETTINGS) {
                com.tamagotchi.code.feature.settings.SettingsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToLanguage = { navController.navigate(Routes.SETTINGS_LANGUAGE) },
                    onNavigateToAbout = { navController.navigate(Routes.SETTINGS_ABOUT) },
                    onNavigateToGitHub = { navController.navigate(Routes.SETTINGS_GITHUB) },
                    onNavigateToSkillTree = { navController.navigate(Routes.SKILL_TREE) },
                    onNavigateToSeasonPass = { navController.navigate(Routes.SEASON_PASS) },
                    onNavigateToWeeklyMissions = { navController.navigate(Routes.WEEKLY_MISSIONS) }
                )
            }
            composable(Routes.SETTINGS_LANGUAGE) {
                com.tamagotchi.code.feature.settings.SettingsLanguageScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.SETTINGS_ABOUT) {
                com.tamagotchi.code.feature.settings.AboutScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.GAMES) {
                com.tamagotchi.code.feature.games.GamesScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToBugHunt = { navController.navigate(Routes.BUG_HUNT) },
                    onNavigateToGitRescue = { navController.navigate(Routes.GIT_RESCUE) },
                    onNavigateToRefactorRush = { navController.navigate(Routes.REFACTOR_RUSH) },
                    onNavigateToCodeReview = { navController.navigate(Routes.CODE_REVIEW) },
                    onNavigateToHackathon = { navController.navigate(Routes.HACKATHON) }
                )
            }
            composable(Routes.BUG_HUNT) {
                com.tamagotchi.code.feature.games.BugHuntScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.GIT_RESCUE) {
                com.tamagotchi.code.feature.games.GitRescueScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.REFACTOR_RUSH) {
                com.tamagotchi.code.feature.games.RefactorRushScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Routes.CODE_REVIEW) {
                CodeReviewScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.HACKATHON) {
                HackathonScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.SKILL_TREE) {
                SkillTreeScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.SEASON_PASS) {
                SeasonPassScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.WEEKLY_MISSIONS) {
                WeeklyMissionsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.SETTINGS_GITHUB) {
                GitHubSyncScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
}