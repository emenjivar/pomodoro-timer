package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetNighModeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = settingsRepository.setNightMode(value)
}
