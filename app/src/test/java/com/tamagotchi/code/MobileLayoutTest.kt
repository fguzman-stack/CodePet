package com.tamagotchi.code

import android.content.Context
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.data.database.AppDatabase
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.repository.AchievementsRepository
import com.tamagotchi.code.data.repository.PetRepository
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import com.tamagotchi.code.feature.focus.FocusScreen
import com.tamagotchi.code.feature.games.CodeReviewScreen
import com.tamagotchi.code.feature.games.HackathonScreen
import com.tamagotchi.code.feature.learn.LearnScreen
import com.tamagotchi.code.feature.shop.ShopScreen
import com.tamagotchi.code.feature.onboarding.OnboardingScreen
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import com.tamagotchi.code.ui.theme.MyApplicationTheme
import com.tamagotchi.code.ui.theme.contrastRatio
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, qualifiers = "w320dp-h480dp", sdk = [35])
class MobileLayoutTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private lateinit var database: AppDatabase
    private lateinit var model: PetViewModel
    private lateinit var context: Context
    private val store = ViewModelStore()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val repository = PetRepository(database.petDao())
        val preferences = UserPreferencesRepository(context)
        runBlocking {
            repository.savePetState(PetStateEntity())
            preferences.setSoundEnabled(false)
            preferences.setReduceMotion(true)
        }
        compose.runOnUiThread {
            model = PetViewModel(repository, preferences, AchievementsRepository(context))
            store.put("pet", model)
        }
        compose.waitUntil(10_000) {
            shadowOf(Looper.getMainLooper()).idle()
            !model.isFocusBusy.value
        }
    }

    @After
    fun tearDown() {
        compose.runOnUiThread { store.clear() }
        database.close()
    }

    private fun show(content: @Composable (PetStateEntity) -> Unit) {
        compose.setContent {
            MyApplicationTheme {
                val density = LocalDensity.current
                val pet by model.petState.collectAsState()
                CompositionLocalProvider(
                    LocalDensity provides Density(density.density, fontScale = 2f),
                    LocalReduceMotion provides true
                ) {
                    // A short phone viewport also represents landscape or an open keyboard.
                    Box(Modifier.size(width = 300.dp, height = 260.dp)) {
                        content(pet ?: PetStateEntity())
                    }
                }
            }
        }
    }

    @Test
    fun wardrobeOnlyShowsOwnedPreviewAndRealPurchasableItems() {
        show { pet -> ShopScreen(model, pet) }
        compose.onNodeWithTag("wardrobe_phase_selector").assertDoesNotExist()
        compose.onNodeWithTag("wardrobe_pixel_switch").assertDoesNotExist()
        compose.onNodeWithTag("wardrobe_action_beanie").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithTag("wardrobe_try_kotlin_beret").assertDoesNotExist()
        compose.onNodeWithTag("wardrobe_preview").performScrollTo().assertIsDisplayed()
        compose.runOnIdle { assertTrue(model.ownedItems.value.isEmpty()) }
    }

    @Test
    fun focusStartIsReachableWithLargeTextAndSelectedDurationHasContrast() {
        show { pet -> FocusScreen(model, pet, emptyList()) }
        val duration = compose.onNodeWithText(context.getString(R.string.focus_min_label, 25))
        duration.performScrollTo().assertIsDisplayed()
        val layouts = mutableListOf<TextLayoutResult>()
        duration.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
        assertTrue(layouts.isNotEmpty())
        assertTrue(contrastRatio(layouts.single().layoutInput.style.color, Color(0xFF2E7D32)) >= 4.5f)
        compose.onNodeWithTag("timer_start_button")
            .performScrollTo().assertIsDisplayed().assertHeightIsAtLeast(48.dp)
    }

    @Test
    fun quizExplanationAndNextRemainReachableAfterAnswering() {
        show { pet -> LearnScreen(model, pet) }
        compose.waitUntil(10_000) { model.activeChallenges.value.isNotEmpty() }
        val answer = model.activeChallenges.value.first().correctAnswerIndex
        compose.onNodeWithTag("quiz_option_$answer").performScrollTo().performClick()
        compose.onNodeWithTag("quiz_option_$answer").assertIsNotEnabled()
        compose.onNodeWithTag("quiz_next_button")
            .performScrollTo().assertIsDisplayed().assertHeightIsAtLeast(48.dp).performClick()
        compose.runOnIdle { assertEquals(1, model.currentChallengeIndex.value) }
    }

    @Test
    fun codeReviewActionsAndExplanationDoNotCompeteWithCodeForHeight() {
        show { CodeReviewScreen(model, onBack = {}) }
        compose.onNodeWithText(context.getString(R.string.review_request_changes))
            .performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText(context.getString(R.string.review_continue))
            .performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText(context.getString(R.string.review_round, 2))
            .performScrollTo().assertIsDisplayed()
    }

    @Test
    fun hackathonBackActionIsReachableAfterOpeningAllOptions() {
        var wentBack = false
        show { HackathonScreen(model, onBack = { wentBack = true }) }
        compose.onNodeWithText(context.getString(R.string.hack_solve)).performScrollTo().performClick()
        compose.onNodeWithText(context.getString(R.string.game_back))
            .performScrollTo().assertIsDisplayed().assertHeightIsAtLeast(48.dp).performClick()
        assertTrue(wentBack)
    }

    @Test
    fun onboardingCanFinishInAShortViewportWithLargeText() {
        var completed = false
        show { OnboardingScreen(onComplete = { _, _ -> completed = true }) }
        repeat(3) {
            compose.onNodeWithText(context.getString(R.string.onboarding_next))
                .performScrollTo().performClick()
            compose.waitForIdle()
        }
        compose.onNodeWithText(context.getString(R.string.onboarding_step4_cta, context.getString(R.string.default_pet_name)))
            .performScrollTo().assertIsDisplayed().performClick()
        assertTrue(completed)
    }
}
