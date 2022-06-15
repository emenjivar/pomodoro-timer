package com.emenjivar.pomodoro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = red,
    // For text color
    primaryVariant = Color.White,
    secondary = yellow,
    //
    background = shark,
    // For buttons
    surface = red,
)

private val LightColorPalette = lightColors(
    primary = red,
    primaryVariant = Color.Black,
    secondary = yellow,
    background = Color.White,
    // For buttons
    surface = Color.White,
)

@Composable
fun PomodoroSchedulerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
