package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetRestTimeUseCase(private val settingsRepository: SettingsRepository) {
    suspend fun invoke(value: Long) = settingsRepository.setRestTime(value)
}
