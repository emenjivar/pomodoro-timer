package com.emenjivar.pomodoro.data

import com.emenjivar.pomodoro.data.model.Pomodoro
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getPomodoro(): Flow<Pomodoro>
    suspend fun setWorkTime(value: Long)
    suspend fun setRestTime(value: Long)
    suspend fun isNightMode(): Boolean
    suspend fun setAutoPlay(value: Boolean)
    fun getAutoPlay(): Flow<Boolean>
    suspend fun setNightMode(value: Boolean)
    fun isKeepScreenOn(): Flow<Boolean>
    suspend fun setKeepScreenOn(value: Boolean)
    fun isVibrationEnabled(): Flow<Boolean>
    suspend fun setVibration(value: Boolean)
    fun areSoundsEnabled(): Flow<Boolean>
    suspend fun setSounds(value: Boolean)
    fun getColor(): Flow<Long?>
    suspend fun setColor(value: Long)
}
