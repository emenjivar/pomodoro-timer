package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetColorUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Int) = repository.setColor(value)
}
