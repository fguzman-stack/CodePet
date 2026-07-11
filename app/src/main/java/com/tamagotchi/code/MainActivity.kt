package com.tamagotchi.code

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tamagotchi.code.ui.screens.CodeTamagotchiScreen
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val petViewModel: PetViewModel = viewModel()
        if (petViewModel.hasSeenOnboarding.value) {
            CodeTamagotchiScreen(
              viewModel = petViewModel,
              modifier = Modifier.fillMaxSize()
            )
        } else {
            com.tamagotchi.code.ui.screens.OnboardingScreen(
                onComplete = { petName -> petViewModel.completeOnboarding(petName) }
            )
        }
      }
    }
  }
}
