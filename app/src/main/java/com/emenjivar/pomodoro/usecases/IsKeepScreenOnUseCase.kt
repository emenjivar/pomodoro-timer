package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class IsKeepScreenOnUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.isKeepScreenOn()
}
