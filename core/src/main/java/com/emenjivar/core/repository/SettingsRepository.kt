package com.emenjivar.core.repository

import com.emenjivar.core.model.Pomodoro

interface SettingsRepository {
    suspend fun getPomodoro(): Pomodoro
    suspend fun setWorkTime(value: Long)
    suspend fun setRestTime(value: Long)
    suspend fun isNightMode(): Boolean
    suspend fun setAutoPlay(value: Boolean)
    suspend fun getAutoPlay(): Boolean
    suspend fun setNightMode(value: Boolean)
    suspend fun isKeepScreenOn(): Boolean
    suspend fun setKeepScreenOn(value: Boolean)
    suspend fun isVibrationEnabled(): Boolean
    suspend fun setVibration(value: Boolean)
    suspend fun areSoundsEnabled(): Boolean
    suspend fun setSounds(value: Boolean)
    suspend fun getColor(): Int?
    suspend fun setColor(value: Int)
}
