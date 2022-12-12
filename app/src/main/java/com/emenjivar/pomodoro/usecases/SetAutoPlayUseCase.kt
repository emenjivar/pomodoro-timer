package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class SetAutoPlayUseCase(private val repository: SettingsRepository) {
    suspend fun invoke(value: Boolean) = repository.setAutoPlay(value)
}
