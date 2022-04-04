package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class IsNightModeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke() = settingsRepository.isNightMode()
}
