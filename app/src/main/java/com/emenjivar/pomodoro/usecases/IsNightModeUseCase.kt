package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class IsNightModeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke() = settingsRepository.isNightMode()
}
