package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetVibrationUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setVibration(value)
}