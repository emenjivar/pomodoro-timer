package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class IsVibrationEnabledUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.isVibrationEnabled()
}