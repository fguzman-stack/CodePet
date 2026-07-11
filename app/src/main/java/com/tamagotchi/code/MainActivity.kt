package com.tamagotchi.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.navigation.AppNavigation
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val petViewModel: PetViewModel = koinViewModel()
      val reduceMotion = petViewModel.reduceMotion.collectAsStateWithLifecycle()
      val currentThemeName = petViewModel.currentTheme.value
      val appTheme = ThemeRegistry.getTheme(currentThemeName)

      MyApplicationTheme(
        appTheme = appTheme,
        reduceMotion = reduceMotion.value
      ) {
        AppNavigation(viewModel = petViewModel)
      }
    }
  }
}
