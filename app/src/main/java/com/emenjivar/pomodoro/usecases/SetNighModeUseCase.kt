package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetNighModeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = settingsRepository.setNightMode(value)
}
