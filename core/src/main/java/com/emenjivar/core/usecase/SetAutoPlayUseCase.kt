package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class SetAutoPlayUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setAutoPlay(value)
}
