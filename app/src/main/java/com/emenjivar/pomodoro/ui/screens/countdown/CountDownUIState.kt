package com.emenjivar.pomodoro.ui.screens.countdown

import androidx.compose.ui.graphics.Color
import com.emenjivar.pomodoro.utils.model.Counter
import kotlinx.coroutines.flow.StateFlow

data class CountDownUIState(
    val colorTheme: StateFlow<Color>,
    val counter: StateFlow<Counter?>
)