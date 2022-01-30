package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class GetPomodoroTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke() = settingsRepository.getPomodoroTime()
}