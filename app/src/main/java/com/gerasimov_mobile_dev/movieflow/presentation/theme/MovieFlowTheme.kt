package com.gerasimov_mobile_dev.movieflow.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9985C9),
    secondary = Color(0xFFA198AC),
    tertiary = Color(0xFFCCC2DC),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color(0xFF312E38),
    onSecondary = Color(0xFF312E38),
    onTertiary = Color(0xFF312E38),
    onBackground = Color(0xFFF1E8E5),
    onSurface = Color(0xFFF1E8E5),
)

@Composable
fun MovieFlowTheme(content: @Composable () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
        typography = MaterialTheme.typography,
        content = content,
    )
}