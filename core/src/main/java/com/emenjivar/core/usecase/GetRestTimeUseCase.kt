package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class GetRestTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke() = settingsRepository.getRestTime()
}