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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

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

// ─────────────────────────────────────────────────────────────
// WCAG 2.x contrast utilities (https://www.w3.org/TR/WCAG22/#contrast-minimum)
// Used to guarantee every theme's text stays readable over its surfaces.
// ─────────────────────────────────────────────────────────────

fun Color.luminance(): Float {
    fun lin(c: Float) = if (c <= 0.03928f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    return 0.2126f * lin(red) + 0.7152f * lin(green) + 0.0722f * lin(blue)
}

fun contrastRatio(a: Color, b: Color): Float {
    val la = a.luminance()
    val lb = b.luminance()
    return (max(la, lb) + 0.05f) / (min(la, lb) + 0.05f)
}

fun bestContentOn(background: Color): Color =
    if (contrastRatio(Color.White, background) >= contrastRatio(Color.Black, background)) Color.White else Color.Black

fun Color.compositeOver(background: Color): Color =
    if (alpha >= 1f) this else lerp(background, copy(alpha = 1f), alpha)

/**
 * Returns [text] unchanged when it already reaches [min] contrast against [background];
 * otherwise blends it toward white/black (whichever direction helps) until it passes.
 */
fun ensureContrast(text: Color, background: Color, min: Float = 4.5f): Color {
    if (contrastRatio(text, background) >= min) return text
    val target = bestContentOn(background)
    fun mix(a: Float, b: Float, t: Float) = a + (b - a) * t
    var best = text
    for (i in 1..24) {
        val candidate = text.copy(
            red = mix(text.red, target.red, i / 24f),
            green = mix(text.green, target.green, i / 24f),
            blue = mix(text.blue, target.blue, i / 24f)
        )
        if (contrastRatio(candidate, background) >= min) return candidate
        best = candidate
    }
    return best
}

/**
 * Returns [color] adjusted until it is legible over the current theme's
 * background. Use for text/accents painted directly on the animated background.
 */
@Composable
fun readableOnBackground(color: Color): Color = ensureContrast(color, LocalAppTheme.current.background)

/**
 * Builds a Material 3 [ColorScheme] from our custom [AppTheme].
 * Uses [darkColorScheme] or [lightColorScheme] based on [AppTheme.isDark].
 * All "on" roles are contrast-checked against their actual (composited) containers.
 */
fun AppTheme.toColorScheme(): ColorScheme {
    val surfaceTint = primary
    val invSurface = if (isDark) Color(0xFFE0E0E0) else Color(0xFF1C1C1C)
    val invOnSurface = if (isDark) Color(0xFF1C1C1C) else Color(0xFFE0E0E0)

    fun onColor(foreground: Color, container: Color, min: Float = 4.5f): Color =
        if (contrastRatio(foreground, container) >= min) foreground
        else ensureContrast(bestContentOn(container), container, min)

    val effOnPrimary = onColor(onPrimary, primary)
    val effOnSecondary = onColor(bestContentOn(secondary), secondary)
    val effOnTertiary = onColor(bestContentOn(tertiary), tertiary)

    val primaryContainerColor = primary.copy(alpha = 0.20f)
    val secondaryContainerColor = secondary.copy(alpha = 0.18f)
    val tertiaryContainerColor = tertiary.copy(alpha = 0.18f)
    val errorContainerColor = error.copy(alpha = 0.20f)

    val effOnPrimaryContainer = onColor(textPrimary, primaryContainerColor.compositeOver(surface))
    val effOnSecondaryContainer = onColor(textPrimary, secondaryContainerColor.compositeOver(surface))
    val effOnTertiaryContainer = onColor(textPrimary, tertiaryContainerColor.compositeOver(surface))
    val effOnErrorContainer = onColor(textPrimary, errorContainerColor.compositeOver(surface))
    val effOnError = onColor(Color.White, error)
    val effOnBackground = onColor(textPrimary, background)
    val effOnSurface = onColor(textPrimary, surface)
    val effOnSurfaceVariant = onColor(textSecondary, surfaceVariant)

    return if (isDark) {
        darkColorScheme(
            primary = primary,
            onPrimary = effOnPrimary,
            primaryContainer = primaryContainerColor,
            onPrimaryContainer = effOnPrimaryContainer,
            secondary = secondary,
            onSecondary = effOnSecondary,
            secondaryContainer = secondaryContainerColor,
            onSecondaryContainer = effOnSecondaryContainer,
            tertiary = tertiary,
            onTertiary = effOnTertiary,
            tertiaryContainer = tertiaryContainerColor,
            onTertiaryContainer = effOnTertiaryContainer,
            error = error,
            onError = effOnError,
            errorContainer = errorContainerColor,
            onErrorContainer = effOnErrorContainer,
            background = background,
            onBackground = effOnBackground,
            surface = surface,
            onSurface = effOnSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = effOnSurfaceVariant,
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
            onPrimary = effOnPrimary,
            primaryContainer = primaryContainerColor,
            onPrimaryContainer = effOnPrimaryContainer,
            secondary = secondary,
            onSecondary = effOnSecondary,
            secondaryContainer = secondaryContainerColor,
            onSecondaryContainer = effOnSecondaryContainer,
            tertiary = tertiary,
            onTertiary = effOnTertiary,
            tertiaryContainer = tertiaryContainerColor,
            onTertiaryContainer = effOnTertiaryContainer,
            error = error,
            onError = effOnError,
            errorContainer = errorContainerColor,
            onErrorContainer = effOnErrorContainer,
            background = background,
            onBackground = effOnBackground,
            surface = surface,
            onSurface = effOnSurface,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = effOnSurfaceVariant,
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
