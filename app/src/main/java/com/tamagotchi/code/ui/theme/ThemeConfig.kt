package com.tamagotchi.code.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.tamagotchi.code.R
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Complete theme definition that goes beyond colors to give each theme
 * its own personality through typography, shape style, and visual details.
 */
data class AppTheme(
    // Identity
    val name: String,
    val icon: ImageVector,
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
        // 0. DEFAULT – Tema base, siempre disponible, adaptable
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Default",
            icon = Icons.Default.Palette,
            description = "Tema base. Adaptable al modo claro u oscuro del sistema.",
            isDark = false,
            background = Color(0xFFF5F7FF),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFE9EEFF),
            primary = Color(0xFF5D67FF),
            secondary = Color(0xFF8C94FF),
            tertiary = Color(0xFFA3AFFF),
            onPrimary = Color.White,
            textPrimary = Color(0xFF0F1128),
            textSecondary = Color(0xFF5A6390),
            accent = Color(0xFF5D67FF),
            success = Color(0xFF4CAF50),
            error = Color(0xFFB3261E),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Bold,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFFF5F7FF), Color(0xFFE9EEFF), Color(0xFFE0E6FF))
        ),

        // ──────────────────────────────────────────────────────────
        // 1. MATRIX GREEN – Terminal hacker, monospace puro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Matrix Green",
            icon = Icons.Default.Code,
            description = "Terminal hacker. Código verde sobre negro.",
            isDark = true,
            background = Color(0xFF040A06),
            surface = Color(0xFF0A140C),
            surfaceVariant = Color(0xFF122416),
            primary = Color(0xFF00FF41),
            secondary = Color(0xFF008F11),
            tertiary = Color(0xFF4AF626),
            onPrimary = Color(0xFF001A06),
            textPrimary = Color(0xFFD1FFD7),
            textSecondary = Color(0xFF7DE88A),
            accent = Color(0xFF00FF41),
            success = Color(0xFF00FF41),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.Bold,
            cornerRadius = 2.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0A140C), Color(0xFF040A06))
        ),

        // ──────────────────────────────────────────────────────────
        // 2. GALÁCTICO – Etéreo, profundo, espacial
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Galáctico",
            icon = Icons.Default.AutoAwesome,
            description = "Viaja entre estrellas. Púrpuras profundos y destellos cósmicos.",
            isDark = true,
            background = Color(0xFF05030D),
            surface = Color(0xFF0E091C),
            surfaceVariant = Color(0xFF181030),
            primary = Color(0xFFB57AFF),
            secondary = Color(0xFF755BB4),
            tertiary = Color(0xFF4C3B7F),
            onPrimary = Color.White,
            textPrimary = Color(0xFFF2EDFF),
            textSecondary = Color(0xFFC4B8E8),
            accent = Color(0xFFD946EF),
            success = Color(0xFF2DD4BF),
            error = Color(0xFFFB7185),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 24.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF181030), Color(0xFF0E091C), Color(0xFF05030D))
        ),

        // ──────────────────────────────────────────────────────────
        // 3. CYBERPUNK – Neón agresivo, glitch urbano
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Cyberpunk",
            icon = Icons.Default.FlashOn,
            description = "Ciudad neón. Rosa eléctrico y cian contra la oscuridad.",
            isDark = true,
            background = Color(0xFF08040C),
            surface = Color(0xFF140A21),
            surfaceVariant = Color(0xFF200F36),
            primary = Color(0xFFFF0055),
            secondary = Color(0xFF00F0FF),
            tertiary = Color(0xFFFFD600),
            onPrimary = Color.Black,
            textPrimary = Color(0xFFF8F4FF),
            textSecondary = Color(0xFF75E6F0),
            accent = Color(0xFFFFD600),
            success = Color(0xFF00E676),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.Monospace,
            titleWeight = FontWeight.Black,
            cornerRadius = 0.dp,
            borderWidth = 3.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF200F36), Color(0xFF140A21), Color(0xFF08040C))
        ),

        // ──────────────────────────────────────────────────────────
        // 4. SAKURA – Delicado, cálido, japonés
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Sakura",
            icon = Icons.Default.LocalFlorist,
            description = "Pétalos al viento. Elegancia japonesa en rosa suave.",
            isDark = false,
            background = Color(0xFFFFFBFB),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFF7ECF0),
            primary = Color(0xFFE88EA2),
            secondary = Color(0xFFF4B8C8),
            tertiary = Color(0xFFDCA6B4),
            onPrimary = Color.White,
            textPrimary = Color(0xFF4A3B3E),
            textSecondary = Color(0xFF968388),
            accent = Color(0xFFE88EA2),
            success = Color(0xFF4E9E81),
            error = Color(0xFFD15C5C),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFFFFFBFB), Color(0xFFFDF5F7), Color(0xFFF7ECF0))
        ),

        // ──────────────────────────────────────────────────────────
        // 5. MINIMALISTA – Limpio, espacioso, sin ruido
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Minimalista",
            icon = Icons.Default.CheckBoxOutlineBlank,
            description = "Menos es más. Blanco puro con acentos sutiles.",
            isDark = false,
            background = Color(0xFFF7F7F7),
            surface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFFEEEEEE),
            primary = Color(0xFF111111),
            secondary = Color(0xFF666666),
            tertiary = Color(0xFFE0E0E0),
            onPrimary = Color.White,
            textPrimary = Color(0xFF111111),
            textSecondary = Color(0xFF777777),
            accent = Color(0xFF333333),
            success = Color(0xFF333333),
            error = Color(0xFFD32F2F),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Normal,
            cornerRadius = 12.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 6. NEÓN – Negro absoluto con destellos vibrantes
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Neón",
            icon = Icons.Default.Lightbulb,
            description = "Oscuridad total. Destellos que cortan la noche.",
            isDark = true,
            background = Color(0xFF000000),
            surface = Color(0xFF080808),
            surfaceVariant = Color(0xFF121212),
            primary = Color(0xFFD000FF),
            secondary = Color(0xFF00FFD1),
            tertiary = Color(0xFFFF003C),
            onPrimary = Color.Black,
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFAAAAAA),
            accent = Color(0xFF00FFD1),
            success = Color(0xFF00FF00),
            error = Color(0xFFFF003C),
            fontFamily = FontFamily.Monospace,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Bold,
            cornerRadius = 16.dp,
            borderWidth = 1.dp,
            usesGradients = false
        ),

        // ──────────────────────────────────────────────────────────
        // 7. OCÉANO – Azules profundos, calma submarina
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Océano",
            icon = Icons.Default.WaterDrop,
            description = "Sumérgete en la calma. Azules profundos y espuma marina.",
            isDark = true,
            background = Color(0xFF020B14),
            surface = Color(0xFF061524),
            surfaceVariant = Color(0xFF0C2238),
            primary = Color(0xFF00B4D8),
            secondary = Color(0xFF48CAE4),
            tertiary = Color(0xFF90E0EF),
            onPrimary = Color(0xFF001A29),
            textPrimary = Color(0xFFE0F7FA),
            textSecondary = Color(0xFFA0D0E0),
            accent = Color(0xFF00B4D8),
            success = Color(0xFF00E676),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Medium,
            cornerRadius = 20.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0C2238), Color(0xFF061524), Color(0xFF020B14))
        ),

        // ──────────────────────────────────────────────────────────
        // 8. VOLCÁNICO – Intenso, magma oscuro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Volcánico",
            icon = Icons.Default.LocalFireDepartment,
            description = "Fuego bajo la superficie. Poder y fuerza bruta.",
            isDark = true,
            background = Color(0xFF0D0300),
            surface = Color(0xFF1A0600),
            surfaceVariant = Color(0xFF260A00),
            primary = Color(0xFFFF4500),
            secondary = Color(0xFFFF8C00),
            tertiary = Color(0xFF8B0000),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFF0E6),
            textSecondary = Color(0xFFD4A090),
            accent = Color(0xFFFF8C00),
            success = Color(0xFF76FF03),
            error = Color(0xFFFF1744),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.Black,
            cornerRadius = 4.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF260A00), Color(0xFF1A0600), Color(0xFF0D0300))
        ),

        // ──────────────────────────────────────────────────────────
        // 9. SAMURAI – Elegante, rojo/negro/oro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Samurai",
            icon = Icons.Default.Security,
            description = "Honor y disciplina. Acero, sangre y oro antiguo.",
            isDark = true,
            background = Color(0xFF0A0A0A),
            surface = Color(0xFF141414),
            surfaceVariant = Color(0xFF211C1C),
            primary = Color(0xFFD32F2F),
            secondary = Color(0xFFC0A080),
            tertiary = Color(0xFF8B0000),
            onPrimary = Color.White,
            textPrimary = Color(0xFFEBEBEB),
            textSecondary = Color(0xFFB0A8A5),
            accent = Color(0xFFC0A080),
            success = Color(0xFF43A047),
            error = Color(0xFFD50000),
            fontFamily = FontFamily.Serif,
            titleFontFamily = FontFamily.Serif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 0.dp,
            borderWidth = 1.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF211C1C), Color(0xFF141414), Color(0xFF0A0A0A))
        ),

        // ──────────────────────────────────────────────────────────
        // 10. AURORA – Gradientes polares, mágico
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Aurora",
            icon = Icons.Default.Waves,
            description = "Luces del norte. Gradientes que danzan en el cielo oscuro.",
            isDark = true,
            background = Color(0xFF030814),
            surface = Color(0xFF081226),
            surfaceVariant = Color(0xFF0D1D3A),
            primary = Color(0xFF00FFA3),
            secondary = Color(0xFF8A2BE2),
            tertiary = Color(0xFF00BFFF),
            onPrimary = Color(0xFF01140D),
            textPrimary = Color(0xFFE6FFFA),
            textSecondary = Color(0xFFA8C0CC),
            accent = Color(0xFF8A2BE2),
            success = Color(0xFF00FFA3),
            error = Color(0xFFFF5252),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.Light,
            cornerRadius = 24.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF0D1D3A), Color(0xFF081226), Color(0xFF030814))
        ),

        // ──────────────────────────────────────────────────────────
        // 12. NOCTURNO – iOS-style, elegante modo oscuro
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Nocturno",
            icon = Icons.Default.NightsStay,
            description = "Noche elegante. Inspiración iOS con azul profundo.",
            isDark = true,
            background = Color(0xFF0A0D24),
            surface = Color(0xFF121530),
            surfaceVariant = Color(0xFF1E2140),
            primary = Color(0xFF6D7AFF),
            secondary = Color(0xFF8C94FF),
            tertiary = Color(0xFFA3AFFF),
            onPrimary = Color.White,
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFD0D4FF),
            accent = Color(0xFF6D7AFF),
            success = Color(0xFF4CAF50),
            error = Color(0xFFFF6B6B),
            fontFamily = FontFamily.SansSerif,
            titleFontFamily = FontFamily.SansSerif,
            titleWeight = FontWeight.SemiBold,
            cornerRadius = 16.dp,
            borderWidth = 0.dp,
            usesGradients = true,
            gradientColors = listOf(Color(0xFF1E2140), Color(0xFF121530), Color(0xFF0A0D24))
        ),

        // ──────────────────────────────────────────────────────────
        // 12. RETRO PIXEL – 8-bit, pixelado, nostálgico — THEME FINAL
        // ──────────────────────────────────────────────────────────
        AppTheme(
            name = "Retro Pixel",
            icon = Icons.Default.VideogameAsset,
            description = "8-bit forever. Experiencia pixel art definitiva.",
            isDark = true,
            background = Color(0xFF0B0B3B),
            surface = Color(0xFF1A1A5E),
            surfaceVariant = Color(0xFF28287A),
            primary = Color(0xFFFF4136),
            secondary = Color(0xFF2ECC40),
            tertiary = Color(0xFFFFDC00),
            onPrimary = Color(0xFFFFFFFF),
            textPrimary = Color(0xFFF0F0F0),
            textSecondary = Color(0xFFCCCCCC),
            accent = Color(0xFF00E5FF),
            success = Color(0xFF2ECC40),
            error = Color(0xFFFF4136),
            fontFamily = FontFamily(Font(R.font.codepet_pixel_font)),
            titleFontFamily = FontFamily(Font(R.font.codepet_pixel_font)),
            titleWeight = FontWeight.ExtraBold,
            cornerRadius = 0.dp,
            borderWidth = 3.dp,
            usesGradients = false
        )
    )

    fun getTheme(name: String, isDark: Boolean = false): AppTheme {
        val theme = allThemes.find { it.name == name } ?: allThemes.first()
        if (name != "Default" || !isDark) return theme
        return theme.copy(
            isDark = true,
            background = Color(0xFF0D0D1A),
            surface = Color(0xFF1A1A33),
            surfaceVariant = Color(0xFF26264D),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFD0D4FF),
            primary = Color(0xFF7B83FF),
            secondary = Color(0xFFA5ABFF),
        )
    }
}
