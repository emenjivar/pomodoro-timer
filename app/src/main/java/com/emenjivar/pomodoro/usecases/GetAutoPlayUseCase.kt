package com.emenjivar.pomodoro.usecases

import com.emenjivar.pomodoro.data.SettingsRepository

class GetAutoPlayUseCase(private val repository: SettingsRepository) {
    suspend fun invoke() = repository.getAutoPlay()
}
