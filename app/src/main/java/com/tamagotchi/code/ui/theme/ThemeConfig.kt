package com.tamagotchi.code.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Complete theme definition that goes beyond colors to give each theme
 * its own personality through typography, shape style, and visual details.
 */
data class AppTheme(
    // Identity
    val name: String,
    val emoji: String,
    val description: String,
    val isDark: Boolean,

    // Color palette
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val onPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val success: Color,
    val error: Color,

    // Typography
    val fontFamily: FontFamily = FontFamily.Default,
    val titleFontFamily: FontFamily = FontFamily.Default,
    val titleWeight: FontWeight = FontWeight.Bold,

    // Visual style
    val cornerRadius: Dp = 12.dp,
    val borderWidth: Dp = 1.dp,
    val usesGradients: Boolean = false,
    val gradientColors: List<Color> = emptyList()
)

object ThemeRegistry {
    val allThemes = listOf(

        // ──────────────────────────────────────────────────────────
        // 1. MATRIX GREEN – Terminal hacker, monospace puro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Matrix Green",
            emoji = "🖥️",
            description = "Terminal hacker. Código verde sobre negro.",
            isDark = true,
            background = Color(0xFF060D07),
            surface = Color(0xFF0E1A10),
            surfaceVariant = Color(0xFF142218),
            primary = Color(0xFF00E676),
            secondary = Color(0xFF69F0AE),
            tertiary = Color(0xFFB9F6CA),
            onPrimary = Color(0xFF003300),
            textPrimary = Color(0xFFE0F2E0),
            textSecondary = Color(0xFF81C784),
            accent = Color(0xFF00E676),
            success = Color(0xFF00E676),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.Bold,
            cornerRadius = 4.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 2. GALÁCTICO – Etéreo, profundo, espacial
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Galáctico",
            emoji = "🌌",
            description = "Viaja entre estrellas. Púrpuras profundos y destellos cósmicos.",
            isDark = true,
            background = Color(0xFF07060F),
            surface = Color(0xFF110F1F),
            surfaceVariant = Color(0xFF1A1730),
            primary = Color(0xFF9C7CFF),
            secondary = Color(0xFFCEB0FF),
            tertiary = Color(0xFF6C63FF),
            onPrimary = Color.White,
            textPrimary = Color(0xFFF0ECFF),
            textSecondary = Color(0xFFA99CCC),
            accent = Color(0xFFE040FB),
            success = Color(0xFF64FFDA),
            error = Color(0xFFFF5277),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Thin,
            cornerRadius = 20.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF1A1730), Color(0xFF0D0B1E), Color(0xFF07060F))
        ),

        // ──────────────────────────────────────────────────────────
        // 3. CYBERPUNK – Neón agresivo, glitch urbano
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Cyberpunk",
            emoji = "⚡",
            description = "Ciudad neón. Rosa eléctrico y cian contra la oscuridad.",
            isDark = true,
            background = Color(0xFF0A0614),
            surface = Color(0xFF150D28),
            surfaceVariant = Color(0xFF1F1438),
            primary = Color(0xFFFF0A6C),
            secondary = Color(0xFF00F0FF),
            tertiary = Color(0xFFFF6EC7),
            onPrimary = Color.White,
            textPrimary = Color(0xFFEAF6FF),
            textSecondary = Color(0xFF78D5E3),
            accent = Color(0xFFFFEB3B),
            success = Color(0xFF00FF9F),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.ExtraBold,
            cornerRadius = 2.dp,
            borderWidth = 2.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF1F1438), Color(0xFF0F0822))
        ),

        // ──────────────────────────────────────────────────────────
        // 4. SAKURA – Delicado, cálido, japonés
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Sakura",
            emoji = "🌸",
            description = "Pétalos al viento. Elegancia japonesa en rosa suave.",
            isDark = false,
            background = Color(0xFFFFF5F7),
            surface = Color(0xFFFFECF0),
            surfaceVariant = Color(0xFFFFDDE5),
            primary = Color(0xFFD81B60),
            secondary = Color(0xFFF06292),
            tertiary = Color(0xFFFF80AB),
            onPrimary = Color.White,
            textPrimary = Color(0xFF4A1027),
            textSecondary = Color(0xFF8E3A5E),
            accent = Color(0xFFFF4081),
            success = Color(0xFF00897B),
            error = Color(0xFFD32F2F),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Normal,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFFFFF5F7), Color(0xFFFFECF0), Color(0xFFFFDDE5))
        ),

        // ──────────────────────────────────────────────────────────
        // 5. MINIMALISTA – Limpio, espacioso, sin ruido
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Minimalista",
            emoji = "◻️",
            description = "Menos es más. Blanco puro con acentos sutiles.",
            isDark = false,
            background = Color(0xFFFAFAFA),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFF0F0F0),
            primary = Color(0xFF1A1A1A),
            secondary = Color(0xFF666666),
            tertiary = Color(0xFF999999),
            onPrimary = Color.White,
            textPrimary = Color(0xFF1A1A1A),
            textSecondary = Color(0xFF888888),
            accent = Color(0xFF333333),
            success = Color(0xFF2E7D32),
            error = Color(0xFFC62828),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Light,
            cornerRadius = 8.dp,
            borderWidth = 0.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 6. NEÓN – Negro absoluto con destellos vibrantes
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Neón",
            emoji = "💜",
            description = "Oscuridad total. Destellos que cortan la noche.",
            isDark = true,
            background = Color(0xFF000000),
            surface = Color(0xFF0A0A0A),
            surfaceVariant = Color(0xFF141414),
            primary = Color(0xFFBB86FC),
            secondary = Color(0xFF03DAC6),
            tertiary = Color(0xFFCF6679),
            onPrimary = Color.Black,
            textPrimary = Color(0xFFE8E0F0),
            textSecondary = Color(0xFF9D8CBB),
            accent = Color(0xFF03DAC6),
            success = Color(0xFF00E676),
            error = Color(0xFFCF6679),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 12.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 7. OCÉANO – Azules profundos, calma submarina
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Océano",
            emoji = "🌊",
            description = "Sumérgete en la calma. Azules profundos y espuma marina.",
            isDark = true,
            background = Color(0xFF05162A),
            surface = Color(0xFF0A2240),
            surfaceVariant = Color(0xFF112D52),
            primary = Color(0xFF4FC3F7),
            secondary = Color(0xFF80DEEA),
            tertiary = Color(0xFF0097A7),
            onPrimary = Color(0xFF002040),
            textPrimary = Color(0xFFE0F7FA),
            textSecondary = Color(0xFF80CBC4),
            accent = Color(0xFF00BCD4),
            success = Color(0xFF26A69A),
            error = Color(0xFFEF5350),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Normal,
            cornerRadius = 14.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF112D52), Color(0xFF0A2240), Color(0xFF05162A))
        ),

        // ──────────────────────────────────────────────────────────
        // 8. VOLCÁNICO – Intenso, magma oscuro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Volcánico",
            emoji = "🌋",
            description = "Fuego bajo la superficie. Poder y fuerza bruta.",
            isDark = true,
            background = Color(0xFF140800),
            surface = Color(0xFF241208),
            surfaceVariant = Color(0xFF331C0E),
            primary = Color(0xFFFF6D00),
            secondary = Color(0xFFFFAB40),
            tertiary = Color(0xFFFF3D00),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFF3E0),
            textSecondary = Color(0xFFFFCC80),
            accent = Color(0xFFFF9100),
            success = Color(0xFF76FF03),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Black,
            cornerRadius = 6.dp,
            borderWidth = 2.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF331C0E), Color(0xFF241208), Color(0xFF140800))
        ),

        // ──────────────────────────────────────────────────────────
        // 9. SAMURAI – Elegante, rojo/negro/oro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Samurai",
            emoji = "⚔️",
            description = "Honor y disciplina. Acero, sangre y oro antiguo.",
            isDark = true,
            background = Color(0xFF0E0C0C),
            surface = Color(0xFF1C1818),
            surfaceVariant = Color(0xFF2A2424),
            primary = Color(0xFFC62828),
            secondary = Color(0xFFFFD54F),
            tertiary = Color(0xFFBF360C),
            onPrimary = Color.White,
            textPrimary = Color(0xFFF5F0EB),
            textSecondary = Color(0xFFBCAAA4),
            accent = Color(0xFFFFD54F),
            success = Color(0xFF43A047),
            error = Color(0xFFD50000),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 4.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 10. AURORA – Gradientes polares, mágico
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Aurora",
            emoji = "✨",
            description = "Luces del norte. Gradientes que danzan en el cielo oscuro.",
            isDark = true,
            background = Color(0xFF060E1C),
            surface = Color(0xFF0D1B30),
            surfaceVariant = Color(0xFF142644),
            primary = Color(0xFF00E5A0),
            secondary = Color(0xFF7C4DFF),
            tertiary = Color(0xFF18FFFF),
            onPrimary = Color(0xFF003020),
            textPrimary = Color(0xFFE0FFF0),
            textSecondary = Color(0xFF80CBC4),
            accent = Color(0xFF7C4DFF),
            success = Color(0xFF00E676),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Normal,
            cornerRadius = 18.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF00E5A0), Color(0xFF7C4DFF), Color(0xFF18FFFF))
        ),

        // ──────────────────────────────────────────────────────────
        // 11. NOCTURNO – iOS-style, elegante modo oscuro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Nocturno",
            emoji = "🌙",
            description = "Noche elegante. Inspiración iOS con azul profundo.",
            isDark = true,
            background = Color(0xFF000000),
            surface = Color(0xFF1C1C1E),
            surfaceVariant = Color(0xFF2C2C2E),
            primary = Color(0xFF0A84FF),
            secondary = Color(0xFF5E5CE6),
            tertiary = Color(0xFF64D2FF),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF8E8E93),
            accent = Color(0xFF5E5CE6),
            success = Color(0xFF30D158),
            error = Color(0xFFFF453A),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 12.dp,
            borderWidth = 0.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 12. RETRO PIXEL – 8-bit, pixelado, nostálgico
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Retro Pixel",
            emoji = "👾",
            description = "8-bit forever. Colores saturados y esquinas afiladas.",
            isDark = true,
            background = Color(0xFF1A1A2E),
            surface = Color(0xFF16213E),
            surfaceVariant = Color(0xFF1F3050),
            primary = Color(0xFFE94560),
            secondary = Color(0xFFF5C518),
            tertiary = Color(0xFF0F3460),
            onPrimary = Color.White,
            textPrimary = Color(0xFFF0F0F0),
            textSecondary = Color(0xFFB0B0CC),
            accent = Color(0xFFF5C518),
            success = Color(0xFF50FA7B),
            error = Color(0xFFFF5555),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.ExtraBold,
            cornerRadius = 0.dp,
            borderWidth = 2.dp,
            usesGradients = false
        )
    )

    fun getTheme(name: String): AppTheme {
        return allThemes.find { it.name == name } ?: allThemes.first()
    }
}
