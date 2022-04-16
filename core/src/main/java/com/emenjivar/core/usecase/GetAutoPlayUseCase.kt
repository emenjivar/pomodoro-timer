package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class GetAutoPlayUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getAutoPlay()
}
