package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class AreSoundsEnableUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.areSoundsEnabled()
}
