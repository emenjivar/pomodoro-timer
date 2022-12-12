package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetVibrationUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setVibration(value)
}
