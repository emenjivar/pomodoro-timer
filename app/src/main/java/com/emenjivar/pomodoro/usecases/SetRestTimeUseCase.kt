package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetRestTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke(value: Long) = settingsRepository.setRestTime(value)
}
