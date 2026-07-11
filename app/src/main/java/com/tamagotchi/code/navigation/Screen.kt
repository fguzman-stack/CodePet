package com.tamagotchi.code.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Learn : Screen("learn")
    data object Focus : Screen("focus")
    data object Shop : Screen("shop")
}

val bottomNavScreens = listOf(Screen.Home, Screen.Learn, Screen.Focus, Screen.Shop)
