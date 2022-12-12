package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetSoundsEnableUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setSounds(value)
}
