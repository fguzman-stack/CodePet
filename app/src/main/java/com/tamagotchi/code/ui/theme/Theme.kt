package com.tamagotchi.code.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * CompositionLocal for reduce-motion preference.
 * Screens can read this to skip or reduce animations.
 */
val LocalReduceMotion = compositionLocalOf { false }

/**
 * CompositionLocal that exposes the full [AppTheme] object so any composable
 * can read theme-specific values like cornerRadius, emoji, fontFamily, etc.
 */
val LocalAppTheme = staticCompositionLocalOf { ThemeRegistry.allThemes.first() }

/**
 * Builds a Material 3 [ColorScheme] from our custom [AppTheme].
 * Uses [darkColorScheme] or [lightColorScheme] based on [AppTheme.isDark].
 */
fun AppTheme.toColorScheme(): ColorScheme {
    val surfaceTint = primary
    val invSurface = if (isDark) Color(0xFFE0E0E0) else Color(0xFF1C1C1C)
    val invOnSurface = if (isDark) Color(0xFF1C1C1C) else Color(0xFFE0E0E0)
    val onSecondary = if (isDark) Color.Black else Color.White
    val onTertiary = if (isDark) Color.Black else Color.White

    return if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primary.copy(alpha = 0.20f),
            onPrimaryContainer = textPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondary.copy(alpha = 0.18f),
            onSecondaryContainer = textPrimary,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiary.copy(alpha = 0.18f),
            onTertiaryContainer = textPrimary,
            error = error,
            onError = Color.White,
            errorContainer = error.copy(alpha = 0.20f),
            onErrorContainer = textPrimary,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = textSecondary,
            surfaceTint = surfaceTint,
            inverseSurface = invSurface,
            inverseOnSurface = invOnSurface,
            inversePrimary = secondary,
            outline = textSecondary.copy(alpha = 0.5f),
            outlineVariant = textSecondary.copy(alpha = 0.25f),
            scrim = Color.Black,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primary.copy(alpha = 0.20f),
            onPrimaryContainer = textPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            secondaryContainer = secondary.copy(alpha = 0.18f),
            onSecondaryContainer = textPrimary,
            tertiary = tertiary,
            onTertiary = onTertiary,
            tertiaryContainer = tertiary.copy(alpha = 0.18f),
            onTertiaryContainer = textPrimary,
            error = error,
            onError = Color.White,
            errorContainer = error.copy(alpha = 0.20f),
            onErrorContainer = textPrimary,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = textSecondary,
            surfaceTint = surfaceTint,
            inverseSurface = invSurface,
            inverseOnSurface = invOnSurface,
            inversePrimary = secondary,
            outline = textSecondary.copy(alpha = 0.5f),
            outlineVariant = textSecondary.copy(alpha = 0.25f),
            scrim = Color.Black,
        )
    }
}

@Composable
fun MyApplicationTheme(
    appTheme: AppTheme = ThemeRegistry.allThemes.first(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    reduceMotion: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> appTheme.toColorScheme()
    }

    val typography = buildTypography(
        bodyFont = appTheme.fontFamily,
        titleFont = appTheme.titleFontFamily,
        titleWeight = appTheme.titleWeight
    )

    CompositionLocalProvider(
        LocalReduceMotion provides reduceMotion,
        LocalAppTheme provides appTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
