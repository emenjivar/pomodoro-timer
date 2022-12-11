package com.emenjivar.pomodoro.ui.screens.countdown

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow

data class CountDownUIState(
    val colorTheme: StateFlow<Color>
)