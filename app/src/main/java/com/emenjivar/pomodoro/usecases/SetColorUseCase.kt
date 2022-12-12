package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetColorUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Int) = repository.setColor(value)
}
