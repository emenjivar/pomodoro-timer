package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetPomodoroTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(value: Long) = settingsRepository.setPomodoroTime(value)
}
