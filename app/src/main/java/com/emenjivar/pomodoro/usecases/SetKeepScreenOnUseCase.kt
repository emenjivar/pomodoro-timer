package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetKeepScreenOnUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setKeepScreenOn(value)
}
