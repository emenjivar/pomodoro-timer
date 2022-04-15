package com.emenjivar.core.repository

import com.emenjivar.core.model.Pomodoro

interface SettingsRepository {
    suspend fun getPomodoro(): Pomodoro
    suspend fun setWorkTime(value: Long)
    suspend fun setRestTime(value: Long)
    suspend fun isKeepScreen(): Boolean
    suspend fun isNightMode(): Boolean
    suspend fun setKeepScreen(value: Boolean)
    suspend fun setNightMode(value: Boolean)
}
