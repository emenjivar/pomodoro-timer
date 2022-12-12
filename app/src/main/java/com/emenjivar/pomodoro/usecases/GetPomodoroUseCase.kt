package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class GetPomodoroUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getPomodoro()
}
