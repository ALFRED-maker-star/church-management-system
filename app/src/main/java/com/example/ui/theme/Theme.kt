package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BurgundyPrimary,
    onPrimary = BurgundyOnPrimary,
    primaryContainer = BurgundyPrimaryContainer,
    onPrimaryContainer = BurgundyOnPrimaryContainer,
    secondary = NavySecondary,
    onSecondary = NavyOnSecondary,
    secondaryContainer = NavySecondaryContainer,
    onSecondaryContainer = NavyOnSecondaryContainer,
    tertiary = GoldAccent,
    onTertiary = GoldOnAccent,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = GoldOnContainer,
    background = WarmBackground,
    onBackground = WarmOnBackground,
    surface = WarmSurface,
    onSurface = WarmOnSurface,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = WarmOnSurfaceVariant,
    outline = WarmOutline,
    outlineVariant = WarmOutlineVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = BurgundyOnPrimaryContainer,
    primaryContainer = BurgundyPrimary,
    onPrimaryContainer = BurgundyPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = NavyOnSecondaryContainer,
    secondaryContainer = NavySecondary,
    onSecondaryContainer = NavySecondaryContainer,
    tertiary = DarkGold,
    onTertiary = GoldOnContainer,
    tertiaryContainer = GoldAccent,
    onTertiaryContainer = GoldContainer,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC7BDB9),
    outline = Color(0xFF6E6460)
)

@Composable
fun FaithConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
