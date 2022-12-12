package com.emenjivar.pomodoro.data

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

interface SharedSettingsRepository {
    fun setColorTheme(colorTheme: Color)
    fun getColorTheme(): Flow<Color>

}