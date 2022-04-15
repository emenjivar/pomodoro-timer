package com.emenjivar.core.repository

import com.emenjivar.core.model.Pomodoro

interface SettingsRepository {
    suspend fun getPomodoroTime(): Long
    suspend fun getRestTime(): Long
    suspend fun isKeepScreen(): Boolean
    suspend fun isNightMode(): Boolean
    suspend fun setPomodoroTime(value: Long)
    suspend fun setRestTime(value: Long)
    suspend fun setKeepScreen(value: Boolean)
    suspend fun setNightMode(value: Boolean)

    suspend fun getPomodoro(): Pomodoro
}
