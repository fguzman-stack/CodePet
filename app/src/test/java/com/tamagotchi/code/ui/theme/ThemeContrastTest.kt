package com.tamagotchi.code.ui.theme

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure-JVM audit: every theme must keep its text readable (WCAG 2.2 AA).
 * https://www.w3.org/TR/WCAG22/#contrast-minimum
 */
class ThemeContrastTest {

    private val allVariants: List<Pair<String, AppTheme>> =
        ThemeRegistry.allThemes.map { it.name to it } +
            ("Default (dark)" to ThemeRegistry.getTheme("Default", isDark = true))

    @Test
    fun textRolesPassAaAgainstTheirContainers() {
        val failures = mutableListOf<String>()
        for ((label, theme) in allVariants) {
            val scheme = theme.toColorScheme()
            val checks = listOf(
                "onBackground/background" to (scheme.onBackground to theme.background),
                "onSurface/surface" to (scheme.onSurface to theme.surface),
                "onSurfaceVariant/surfaceVariant" to (scheme.onSurfaceVariant to theme.surfaceVariant),
                "onSurface/surfaceVariant" to (scheme.onSurface to theme.surfaceVariant),
                "onPrimary/primary" to (scheme.onPrimary to theme.primary),
                "onSecondary/secondary" to (scheme.onSecondary to theme.secondary),
                "onPrimaryContainer/primaryContainer" to (
                    scheme.onPrimaryContainer to theme.primary.copy(alpha = 0.20f).compositeOver(theme.surface)
                    ),
                "onSecondaryContainer/secondaryContainer" to (
                    scheme.onSecondaryContainer to theme.secondary.copy(alpha = 0.18f).compositeOver(theme.surface)
                    ),
                "onErrorContainer/errorContainer" to (
                    scheme.onErrorContainer to theme.error.copy(alpha = 0.20f).compositeOver(theme.surface)
                    ),
            )
            for ((name, pair) in checks) {
                val ratio = contrastRatio(pair.first, pair.second)
                if (ratio < 4.5f) failures += "$label $name = ${ratio.format()} (min 4.5)"
            }
        }
        assertTrue("Low contrast pairs:\n${failures.joinToString("\n")}", failures.isEmpty())
    }

    @Test
    fun decorationColorsReachNonTextMinimum() {
        val failures = mutableListOf<String>()
        for ((label, theme) in allVariants) {
            val checks = listOf(
                "primary as title over background" to (theme.primary to theme.background),
                "error as status text over background" to (theme.error to theme.background),
                "accent over background" to (theme.accent to theme.background),
            )
            for ((name, pair) in checks) {
                val ratio = contrastRatio(pair.first, pair.second)
                if (ratio < 3.0f) failures += "$label $name = ${ratio.format()} (min 3.0)"
            }
        }
        assertTrue("Weak decorative pairs:\n${failures.joinToString("\n")}", failures.isEmpty())
    }

    private fun Float.format() = (this * 100).toInt() / 100f
}
