package com.tamagotchi.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tamagotchi.code.navigation.AppNavigation
import com.tamagotchi.code.ui.screens.OnboardingScreen
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val petViewModel: PetViewModel = koinViewModel()
        if (petViewModel.hasSeenOnboarding.value) {
            AppNavigation(viewModel = petViewModel)
        } else {
            OnboardingScreen(
                onComplete = { petName -> petViewModel.completeOnboarding(petName) }
            )
        }
      }
    }
  }
}
