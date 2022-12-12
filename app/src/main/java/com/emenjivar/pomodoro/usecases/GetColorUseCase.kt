package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class GetColorUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getColor()
}
