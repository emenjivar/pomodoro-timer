package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class IsKeepScreenOnUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.isKeepScreenOn()
}
