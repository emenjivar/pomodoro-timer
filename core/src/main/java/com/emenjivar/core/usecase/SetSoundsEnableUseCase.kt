package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetSoundsEnableUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setSounds(value)
}
