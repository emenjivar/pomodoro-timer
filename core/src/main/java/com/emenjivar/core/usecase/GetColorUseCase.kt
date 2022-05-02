package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class GetColorUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getColor()
}
