package com.emenjivar.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
    private val autoPlay = booleanPreferencesKey(AUTOPLAY)
    private val keepScreen = booleanPreferencesKey(KEEP_SCREEN)
    private val nightMode = booleanPreferencesKey(NIGHT_MODE)
    private val vibration = booleanPreferencesKey(VIBRATION)
    private val sounds = booleanPreferencesKey(SOUNDS)
    private val color = intPreferencesKey(COLOR)

    override suspend fun getPomodoro(): Pomodoro =
        context.dataStore.data.map { pref ->
            Pomodoro(
                workTime = pref[pomodoroTime] ?: DEFAULT_WORK_TIME,
                restTime = pref[restTime] ?: DEFAULT_REST_TIME
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

    override suspend fun isNightMode(): Boolean = context.dataStore.data
        .map { pref ->
            pref[nightMode] ?: false
        }.first()

    override suspend fun setAutoPlay(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[autoPlay] = value
        }
    }

    override suspend fun getAutoPlay(): Boolean = context.dataStore.data
        .map { pref ->
            pref[autoPlay] ?: false
        }.first()

    override suspend fun setNightMode(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[nightMode] = value
        }
    }

    override suspend fun isKeepScreenOn(): Boolean = context.dataStore.data
        .map { preferences ->
            preferences[keepScreen] ?: false
        }.first()

    override suspend fun setKeepScreenOn(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[keepScreen] = value
        }
    }

    override suspend fun isVibrationEnabled(): Boolean = context.dataStore.data
        .map { pref ->
            pref[vibration] ?: false
        }.first()

    override suspend fun setVibration(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[vibration] = value
        }
    }

    override suspend fun areSoundsEnabled(): Boolean = context.dataStore.data
        .map { pref ->
            pref[sounds] ?: true
        }.first()

    override suspend fun setSounds(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[sounds] = value
        }
    }

    override suspend fun getColor(): Int? = context.dataStore.data
        .map { pref ->
            pref[color]
        }.first()

    override suspend fun setColor(value: Int) {
        context.dataStore.edit { pref ->
            pref[color] = value
        }
    }

    companion object {
        const val SETTINGS_NAME = "settings"
        const val POMODORO_TIME = "pomodoro_time"
        const val REST_TIME = "rest_time"
        const val AUTOPLAY = "autoplay"
        const val KEEP_SCREEN = "keep_screen"
        const val NIGHT_MODE = "night_mode"
        const val VIBRATION = "vibration"
        const val SOUNDS = "sounds"
        const val COLOR = "color"
        const val DEFAULT_WORK_TIME: Long = 1000 * 60 * 25 // 25 min
        const val DEFAULT_REST_TIME: Long = 1000 * 60 * 5 // 5 min
    }
}
