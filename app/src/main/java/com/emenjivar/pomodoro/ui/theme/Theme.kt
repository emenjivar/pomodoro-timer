package com.emenjivar.pomodoro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = red,
    background = Color.White,
    surface = shark,
)

private val DarkColorPalette = darkColors(
    primary = red,
    background = shark,
    surface = Color.White
)

@get:Composable
val Colors.title: Color
    get() = mode(light = red, night = Color.White)

@get:Composable
val Colors.subtitle: Color
    get() = mode(light = shark, night = lightGray)

fun Colors.mode(light: Color, night: Color) = if (isLight) light else night

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
