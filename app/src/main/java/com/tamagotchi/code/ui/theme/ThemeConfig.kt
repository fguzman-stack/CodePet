package com.tamagotchi.code.ui.theme

import androidx.compose.ui.graphics.Color

data class AppThemeColors(
    val name: String,
    val background: Color,
    val surface: Color,
    val primary: Color,
    val secondary: Color,
    val onPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val success: Color,
    val error: Color
)

object ThemeRegistry {
    val allThemes = listOf(
        AppThemeColors(
            "Matrix Green", 
            background = Color(0xFF0C100D), surface = Color(0xFF151D16),
            primary = Color(0xFF2E7D32), secondary = Color(0xFF81C784),
            onPrimary = Color.Black, textPrimary = Color.White, textSecondary = Color.LightGray,
            success = Color(0xFF4CAF50), error = Color(0xFFEF5350)
        ),
        AppThemeColors(
            "Galáctico",
            background = Color(0xFF090A0F), surface = Color(0xFF12141D),
            primary = Color(0xFF6200EA), secondary = Color(0xFFB388FF),
            onPrimary = Color.White, textPrimary = Color.White, textSecondary = Color(0xFF9E9E9E),
            success = Color(0xFF00E676), error = Color(0xFFFF1744)
        ),
        AppThemeColors(
            "Cyberpunk",
            background = Color(0xFF0F0B1E), surface = Color(0xFF1D1135),
            primary = Color(0xFFF50057), secondary = Color(0xFF00E5FF),
            onPrimary = Color.White, textPrimary = Color(0xFFE0F7FA), textSecondary = Color(0xFF84FFFF),
            success = Color(0xFF00E676), error = Color(0xFFFFD600)
        ),
        AppThemeColors(
            "Bosque Encantado",
            background = Color(0xFF0D1F15), surface = Color(0xFF173021),
            primary = Color(0xFF388E3C), secondary = Color(0xFFAED581),
            onPrimary = Color.White, textPrimary = Color(0xFFF1F8E9), textSecondary = Color(0xFFC5E1A5),
            success = Color(0xFF69F0AE), error = Color(0xFFFF5252)
        ),
        AppThemeColors(
            "Sakura",
            background = Color(0xFFFCE4EC), surface = Color(0xFFF8BBD0),
            primary = Color(0xFFE91E63), secondary = Color(0xFFF06292),
            onPrimary = Color.White, textPrimary = Color(0xFF880E4F), textSecondary = Color(0xFFAD1457),
            success = Color(0xFF00C853), error = Color(0xFFD50000)
        ),
        AppThemeColors(
            "Minimalista",
            background = Color(0xFFFFFFFF), surface = Color(0xFFF5F5F5),
            primary = Color(0xFF212121), secondary = Color(0xFF757575),
            onPrimary = Color.White, textPrimary = Color(0xFF212121), textSecondary = Color(0xFF616161),
            success = Color(0xFF388E3C), error = Color(0xFFD32F2F)
        ),
        AppThemeColors(
            "Neón",
            background = Color(0xFF000000), surface = Color(0xFF111111),
            primary = Color(0xFF39FF14), secondary = Color(0xFFFF00FF),
            onPrimary = Color.Black, textPrimary = Color(0xFF39FF14), textSecondary = Color(0xFFFF00FF),
            success = Color(0xFF00FFFF), error = Color(0xFFFF0000)
        ),
        AppThemeColors(
            "Océano",
            background = Color(0xFF001F3F), surface = Color(0xFF003366),
            primary = Color(0xFF0074D9), secondary = Color(0xFF7FDBFF),
            onPrimary = Color.White, textPrimary = Color(0xFFE0FFFF), textSecondary = Color(0xFF87CEEB),
            success = Color(0xFF2ECC40), error = Color(0xFFFF4136)
        ),
        AppThemeColors(
            "Volcánico",
            background = Color(0xFF2A0800), surface = Color(0xFF4A1500),
            primary = Color(0xFFFF4500), secondary = Color(0xFFFF8C00),
            onPrimary = Color.White, textPrimary = Color(0xFFFFF5EE), textSecondary = Color(0xFFFFDAB9),
            success = Color(0xFF32CD32), error = Color(0xFFB22222)
        ),
        AppThemeColors(
            "Ártico",
            background = Color(0xFFE0FFFF), surface = Color(0xFFF0FFFF),
            primary = Color(0xFF00BFFF), secondary = Color(0xFF87CEFA),
            onPrimary = Color.White, textPrimary = Color(0xFF00008B), textSecondary = Color(0xFF4682B4),
            success = Color(0xFF00FA9A), error = Color(0xFFFF6347)
        ),
        AppThemeColors(
            "Vaporwave",
            background = Color(0xFF2B00FF), surface = Color(0xFFFF00AA),
            primary = Color(0xFF00FFFF), secondary = Color(0xFFFF00FF),
            onPrimary = Color.Black, textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFF00FFFF),
            success = Color(0xFF00FF00), error = Color(0xFFFF0000)
        ),
        AppThemeColors(
            "Café",
            background = Color(0xFF3E2723), surface = Color(0xFF4E342E),
            primary = Color(0xFF8D6E63), secondary = Color(0xFFD7CCC8),
            onPrimary = Color.White, textPrimary = Color(0xFFEFEBE9), textSecondary = Color(0xFFBCAAA4),
            success = Color(0xFF81C784), error = Color(0xFFE57373)
        ),
        AppThemeColors(
            "Retro",
            background = Color(0xFFF4A460), surface = Color(0xFFDEB887),
            primary = Color(0xFF8B4513), secondary = Color(0xFFA0522D),
            onPrimary = Color.White, textPrimary = Color(0xFF4A0E4E), textSecondary = Color(0xFF800000),
            success = Color(0xFF228B22), error = Color(0xFFB22222)
        ),
        AppThemeColors(
            "Pixel Art",
            background = Color(0xFF2C3E50), surface = Color(0xFF34495E),
            primary = Color(0xFFE74C3C), secondary = Color(0xFFF1C40F),
            onPrimary = Color.White, textPrimary = Color(0xFFECF0F1), textSecondary = Color(0xFFBDC3C7),
            success = Color(0xFF2ECC71), error = Color(0xFFE67E22)
        ),
        AppThemeColors(
            "Samurai",
            background = Color(0xFF1C1C1C), surface = Color(0xFF2D2D2D),
            primary = Color(0xFFC62828), secondary = Color(0xFFEF5350),
            onPrimary = Color.White, textPrimary = Color(0xFFF5F5F5), textSecondary = Color(0xFFBDBDBD),
            success = Color(0xFF2E7D32), error = Color(0xFFD32F2F)
        ),
        AppThemeColors(
            "Medieval",
            background = Color(0xFF2E2B2A), surface = Color(0xFF4E4B49),
            primary = Color(0xFFFFD700), secondary = Color(0xFFDAA520),
            onPrimary = Color.Black, textPrimary = Color(0xFFFAF0E6), textSecondary = Color(0xFFD3D3D3),
            success = Color(0xFF3CB371), error = Color(0xFFCD5C5C)
        ),
        AppThemeColors(
            "Desierto",
            background = Color(0xFFEDC9AF), surface = Color(0xFFF4A460),
            primary = Color(0xFFD2691E), secondary = Color(0xFFCD853F),
            onPrimary = Color.White, textPrimary = Color(0xFF5C4033), textSecondary = Color(0xFF8B4513),
            success = Color(0xFF6B8E23), error = Color(0xFFA52A2A)
        ),
        AppThemeColors(
            "Aurora",
            background = Color(0xFF0B192C), surface = Color(0xFF1A365D),
            primary = Color(0xFF00FF7F), secondary = Color(0xFF40E0D0),
            onPrimary = Color.Black, textPrimary = Color(0xFFE0FFFF), textSecondary = Color(0xFF98FB98),
            success = Color(0xFF32CD32), error = Color(0xFFFF6347)
        ),
        AppThemeColors(
            "Cristal",
            background = Color(0xFFF0F8FF), surface = Color(0xFFE6E6FA),
            primary = Color(0xFF9370DB), secondary = Color(0xFFD8BFD8),
            onPrimary = Color.White, textPrimary = Color(0xFF4B0082), textSecondary = Color(0xFF8A2BE2),
            success = Color(0xFF20B2AA), error = Color(0xFFDB7093)
        ),
        AppThemeColors(
            "Nocturno",
            background = Color(0xFF000000), surface = Color(0xFF1C1C1E),
            primary = Color(0xFF0A84FF), secondary = Color(0xFF5E5CE6),
            onPrimary = Color.White, textPrimary = Color(0xFFFFFFFF), textSecondary = Color(0xFF8E8E93),
            success = Color(0xFF30D158), error = Color(0xFFFF453A)
        ),
        AppThemeColors(
            "Tropical",
            background = Color(0xFFFFFAF0), surface = Color(0xFFFFE4B5),
            primary = Color(0xFFFF7F50), secondary = Color(0xFFFF69B4),
            onPrimary = Color.White, textPrimary = Color(0xFF8B4513), textSecondary = Color(0xFFD2691E),
            success = Color(0xFF32CD32), error = Color(0xFFDC143C)
        ),
        AppThemeColors(
            "Otoño",
            background = Color(0xFF5C2C16), surface = Color(0xFF8B4513),
            primary = Color(0xFFFFA500), secondary = Color(0xFFFF8C00),
            onPrimary = Color.Black, textPrimary = Color(0xFFFFF8DC), textSecondary = Color(0xFFF5DEB3),
            success = Color(0xFF556B2F), error = Color(0xFFB22222)
        ),
        AppThemeColors(
            "Hacker",
            background = Color(0xFF000000), surface = Color(0xFF050505),
            primary = Color(0xFF00FF00), secondary = Color(0xFF008800),
            onPrimary = Color.Black, textPrimary = Color(0xFF00FF00), textSecondary = Color(0xFF005500),
            success = Color(0xFF00FF00), error = Color(0xFF00FF00)
        ),
        AppThemeColors(
            "Magma",
            background = Color(0xFF1A0000), surface = Color(0xFF330000),
            primary = Color(0xFFFF0000), secondary = Color(0xFFFF5500),
            onPrimary = Color.White, textPrimary = Color(0xFFFFDDDD), textSecondary = Color(0xFFFFAAAA),
            success = Color(0xFF00FF00), error = Color(0xFF880000)
        ),
        AppThemeColors(
            "Fantasma",
            background = Color(0xFFE8ECEF), surface = Color(0xFFF4F6F8),
            primary = Color(0xFF6C7A89), secondary = Color(0xFF95A5A6),
            onPrimary = Color.White, textPrimary = Color(0xFF2C3E50), textSecondary = Color(0xFF7F8C8D),
            success = Color(0xFF27AE60), error = Color(0xFFC0392B)
        )
    )

    fun getTheme(name: String): AppThemeColors {
        return allThemes.find { it.name == name } ?: allThemes.first()
    }
}
