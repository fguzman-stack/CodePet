package com.tamagotchi.code.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import com.tamagotchi.code.TestApplication
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [35])
class ThemeContentColorTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun inheritedTextStaysReadableWhenSwitchingBetweenLightAndDarkThemes() {
        val selected = mutableStateOf(ThemeRegistry.allThemes.first())
        compose.setContent {
            MyApplicationTheme(appTheme = selected.value, reduceMotion = true) {
                Column {
                    Text("Inherited root content", Modifier.testTag("root_text"))
                    Scaffold(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onBackground) {
                        Text("Transparent screen content", Modifier.testTag("screen_text"))
                    }
                }
            }
        }
        val variants = ThemeRegistry.allThemes + ThemeRegistry.getTheme("Default", isDark = true)
        for (theme in variants) {
            compose.runOnIdle { selected.value = theme }
            for (tag in listOf("root_text", "screen_text")) {
                val layouts = mutableListOf<TextLayoutResult>()
                compose.onNodeWithTag(tag).performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
                val actual = layouts.single().layoutInput.style.color
                assertTrue("${theme.name}: $tag inherits unreadable $actual", contrastRatio(actual, theme.background) >= 4.5f)
            }
        }
    }

    @Test
    fun oldSamuraiIdentityResolvesToJapaneseWithoutChangingOtherThemes() {
        assertEquals("Japonés", ThemeRegistry.canonicalName("Samurai"))
        assertEquals(ThemeRegistry.getTheme("Japonés"), ThemeRegistry.getTheme("Samurai"))
        assertEquals("Matrix Green", ThemeRegistry.canonicalName("Matrix Green"))
        assertEquals(1, ThemeRegistry.allThemes.count { it.name == "Japonés" })
    }
}
