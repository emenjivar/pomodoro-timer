package com.emenjivar.pomodoro.ui.screens.settings

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow

class SettingsUIState(
    val colorTheme: StateFlow<Color>
)