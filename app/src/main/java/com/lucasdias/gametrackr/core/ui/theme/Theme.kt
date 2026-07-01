package com.lucasdias.gametrackr.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppDarkColorScheme =
    darkColorScheme(
        primary = AppPrimary,
        onPrimary = AppOnPrimary,
        secondary = AppSecondary,
        tertiary = AppTertiary,
        background = AppBackground,
        onBackground = AppTextPrimary,
        surface = AppSurfaceCard,
        onSurface = AppTextPrimary,
        outline = AppOutline,
    )

@Composable
fun GameTrackrTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
