package com.guesscountry.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = GameColors.Mint,
    onPrimary = GameColors.Ink,
    primaryContainer = GameColors.MintDeep,
    onPrimaryContainer = GameColors.White,
    secondary = GameColors.Amber,
    onSecondary = GameColors.Ink,
    secondaryContainer = GameColors.SurfaceRaised,
    onSecondaryContainer = GameColors.Amber,
    tertiary = GameColors.Sky,
    onTertiary = GameColors.Ink,
    background = GameColors.Ink,
    onBackground = GameColors.Text,
    surface = GameColors.Surface,
    onSurface = GameColors.Text,
    surfaceVariant = GameColors.SurfaceMuted,
    onSurfaceVariant = GameColors.TextMuted,
    outline = GameColors.Outline,
    error = GameColors.Error,
    onError = GameColors.White,
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF087F70),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB5F4E4),
    onPrimaryContainer = Color(0xFF00201B),
    secondary = Color(0xFF9A5A00),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDDB5),
    onSecondaryContainer = Color(0xFF2C1600),
    tertiary = Color(0xFF315EA8),
    onTertiary = Color.White,
    background = Color(0xFFF7F9FD),
    onBackground = Color(0xFF121722),
    surface = Color.White,
    onSurface = Color(0xFF121722),
    surfaceVariant = Color(0xFFE5EAF3),
    onSurfaceVariant = Color(0xFF46516A),
    outline = Color(0xFF75809A),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

@Composable
fun GuessCountryTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = GameTypography,
        content = content,
    )
}
