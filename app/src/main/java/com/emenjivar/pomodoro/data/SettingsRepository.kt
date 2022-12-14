package com.emenjivar.pomodoro.data

import com.emenjivar.core.model.Pomodoro
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getPomodoro(): Flow<Pomodoro>
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
    fun areSoundsEnabled(): Flow<Boolean>
    suspend fun setSounds(value: Boolean)
    suspend fun getColor(): Int?
    suspend fun setColor(value: Int)
}
