package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetWorkTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(value: Long) = settingsRepository.setWorkTime(value)
}
