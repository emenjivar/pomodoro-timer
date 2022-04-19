package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetKeepScreenOnUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setKeepScreenOn(value)
}
