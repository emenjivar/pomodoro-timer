package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository

class GetPomodoroUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getPomodoro()
}
