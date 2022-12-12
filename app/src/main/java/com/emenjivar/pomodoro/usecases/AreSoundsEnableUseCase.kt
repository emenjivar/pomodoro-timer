package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class AreSoundsEnableUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.areSoundsEnabled()
}
