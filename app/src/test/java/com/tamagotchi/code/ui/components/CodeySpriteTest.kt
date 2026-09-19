package com.tamagotchi.code.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.activity.ComponentActivity
import androidx.core.view.drawToBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.TestApplication
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, sdk = [35])
class CodeySpriteTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun stateFlowUpdatesSpriteWhileReducedMotionKeepsTimeFrozen() {
        val state = MutableStateFlow(PetStateEntity(level = 10, currentStatus = "HAPPY"))
        compose.setContent {
            val pet by state.collectAsState()
            CompositionLocalProvider(LocalReduceMotion provides true) {
                CodeySprite(pet.currentStatus, pet.level, pet.isDead,
                    modifier = Modifier.size(180.dp).testTag("codey"))
            }
        }
        fun snapshot() = compose.runOnIdle { compose.activity.window.decorView.drawToBitmap() }
        val happy = snapshot()
        compose.mainClock.advanceTimeBy(1000)
        assertTrue(happy.sameAs(snapshot()))
        compose.runOnIdle { state.value = state.value.copy(currentStatus = "SICK") }
        val sick = snapshot()
        assertFalse(happy.sameAs(sick))
        compose.runOnIdle { state.value = state.value.copy(level = 50) }
        assertFalse(sick.sameAs(snapshot()))
        compose.runOnIdle { state.value = state.value.copy(isDead = true) }
        val dead = snapshot()
        compose.runOnIdle { state.value = state.value.copy(currentStatus = "EXCITED") }
        assertTrue(dead.sameAs(snapshot()))
    }
}
