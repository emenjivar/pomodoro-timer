package com.emenjivar.pomodoro.data

import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.ui.theme.red
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SharedSettingsRepositoryImp : SharedSettingsRepository {

    private val colorTheme = MutableStateFlow(red)

    override fun setColorTheme(colorTheme: Color) {
        this.colorTheme.update { colorTheme }
    }

    override fun getColorTheme() = colorTheme
}