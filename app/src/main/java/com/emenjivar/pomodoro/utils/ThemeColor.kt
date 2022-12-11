package com.emenjivar.pomodoro.utils

import androidx.compose.ui.graphics.Color
import com.emenjivar.pomodoro.ui.theme.*

sealed class ThemeColor(val color: Color) {
    object Tomato : ThemeColor(red)
    object Orange : ThemeColor(orange)
    object Yellow : ThemeColor(yellow)
    object Green : ThemeColor(green)
    object LeafGreen : ThemeColor(leafGreen)
}
