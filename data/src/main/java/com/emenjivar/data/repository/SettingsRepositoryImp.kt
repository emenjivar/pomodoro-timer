package com.emenjivar.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emenjivar.core.model.Pomodoro
import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepositoryImp(private val context: Context) : SettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_NAME)
    private val pomodoroTime = longPreferencesKey(POMODORO_TIME)
    private val restTime = longPreferencesKey(REST_TIME)
    private val keepScreen = booleanPreferencesKey(KEEP_SCREEN)
    private val nightMode = booleanPreferencesKey(NIGHT_MODE)

    override suspend fun getPomodoro(): Pomodoro =
        context.dataStore.data.map { pref ->
            Pomodoro(
                workTime = pref[pomodoroTime] ?: 0,
                restTime = pref[restTime] ?: 0
            )
        }.first()

    override suspend fun setWorkTime(value: Long) {
        context.dataStore.edit { settings ->
            settings[pomodoroTime] = value
        }
    }

    override suspend fun setRestTime(value: Long) {
        context.dataStore.edit { settings ->
            settings[restTime] = value
        }
    }

    override suspend fun isKeepScreen(): Boolean = context.dataStore.data
        .map { preferences ->
            preferences[keepScreen] ?: false
        }.first()

    override suspend fun isNightMode(): Boolean = context.dataStore.data
        .map { pref ->
            pref[nightMode] ?: false
        }.first()

    override suspend fun setKeepScreen(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[keepScreen] = value
        }
    }

    override suspend fun setNightMode(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[nightMode] = value
        }
    }

    companion object {
        const val SETTINGS_NAME = "settings"
        const val POMODORO_TIME = "pomodoro_time"
        const val REST_TIME = "rest_time"
        const val KEEP_SCREEN = "keep_screen"
        const val NIGHT_MODE = "night_mode"
    }
}
