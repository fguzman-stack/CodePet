package com.tamagotchi.code.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Learn : Screen("learn")
    data object Focus : Screen("focus")
    data object Shop : Screen("shop")
    data object Settings : Screen("settings")
    data object SettingsLanguage : Screen("settings_language")
    data object Games : Screen("games")
    data object BugHunt : Screen("bug_hunt")
    data object GitRescue : Screen("git_rescue")
    data object RefactorRush : Screen("refactor_rush")
}

val bottomNavScreens = listOf(Screen.Home, Screen.Learn, Screen.Focus, Screen.Shop)
