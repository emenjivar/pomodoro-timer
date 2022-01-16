package com.emenjivar.data.repository

interface SettingsRepository {
    suspend fun getPomodoroTime(): Long
    suspend fun getRestTime(): Long
    suspend fun isKeepScreen(): Boolean
    suspend fun isNightMode(): Boolean
    suspend fun setPomodoroTime(value: Long)
    suspend fun setRestTime(value: Long)
    suspend fun setKeepScreen(value: Boolean)
    suspend fun setNightMode(value: Boolean)
}
