package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class IsVibrationEnabledUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.isVibrationEnabled()
}
