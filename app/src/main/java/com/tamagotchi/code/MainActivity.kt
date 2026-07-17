package com.tamagotchi.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.MobileAds
import com.tamagotchi.code.navigation.AppNavigation
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    MobileAds.initialize(this) {}

    enableEdgeToEdge()
    setContent {
      val petViewModel: PetViewModel = koinViewModel()
      val reduceMotion = petViewModel.reduceMotion.collectAsStateWithLifecycle()
      val currentThemeName = petViewModel.currentTheme.value
      val defaultMode = petViewModel.defaultThemeMode.value

      val isDark = if (currentThemeName == "Default") {
        when (defaultMode) {
          "DARK" -> true
          "LIGHT" -> false
          else -> isSystemInDarkTheme()
        }
      } else {
        ThemeRegistry.getTheme(currentThemeName).isDark
      }

      val appTheme = ThemeRegistry.getTheme(currentThemeName, isDark)

      MyApplicationTheme(
        appTheme = appTheme,
        darkTheme = isDark,
        reduceMotion = reduceMotion.value
      ) {
        AppNavigation(viewModel = petViewModel)
      }
    }
  }
}
