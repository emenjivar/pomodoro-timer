package com.emenjivar.pomodoro.screens.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetAutoPlayUseCase
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsKeepScreenOnUseCase
import com.emenjivar.core.usecase.IsVibrationEnabledUseCase
import com.emenjivar.core.usecase.SetAutoPlayUseCase
import com.emenjivar.core.usecase.SetKeepScreenOnUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.core.usecase.SetVibrationUseCase
import com.emenjivar.core.usecase.SetWorkTimeUseCase
import com.emenjivar.pomodoro.utils.millisecondsToMinutes
import com.emenjivar.pomodoro.utils.minutesToMilliseconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPomodoroUseCase: GetPomodoroUseCase,
    private val setWorkTimeUseCase: SetWorkTimeUseCase,
    private val setRestTimeUseCase: SetRestTimeUseCase,
    private val getAutoPlayUseCase: GetAutoPlayUseCase,
    private val setAutoPlayUseCase: SetAutoPlayUseCase,
    private val isKeepScreenOnUseCase: IsKeepScreenOnUseCase,
    private val setKeepScreenOnUseCase: SetKeepScreenOnUseCase,
    private val isVibrationEnabledUseCase: IsVibrationEnabledUseCase,
    private val setVibrationUseCase: SetVibrationUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testMode: Boolean = false
) : ViewModel() {

    private val _pomodoroTime = MutableLiveData(0L)
    val pomodoroTime = _pomodoroTime

    private val _restTime = MutableLiveData(0L)
    val restTime = _restTime

    private val _closeSettings = MutableLiveData(false)
    val closeSettings = _closeSettings

    private val _autoPlay = mutableStateOf(false)
    val autoPlay: State<Boolean> = _autoPlay

    private val _keepScreenOn = mutableStateOf(false)
    val keepScreenOn: State<Boolean> = _keepScreenOn

    private val _vibrationEnabled = mutableStateOf(false)
    val vibrationEnabled: State<Boolean> = _vibrationEnabled

    init {
        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadSettings()
            }
        }
    }

    suspend fun loadSettings() {
        /**
         * Values are expressed in milliseconds
         * transform to minutes to show a readable value on UI
         */
        val pomodoro = getPomodoroUseCase.invoke()
        _pomodoroTime.postValue(pomodoro.workTime.millisecondsToMinutes())
        _restTime.postValue(pomodoro.restTime.millisecondsToMinutes())
        _autoPlay.value = getAutoPlayUseCase.invoke()
        _keepScreenOn.value = isKeepScreenOnUseCase.invoke()
        _vibrationEnabled.value = isVibrationEnabledUseCase.invoke()
    }

    /**
     * @param time: expressed in minutes.
     */
    fun setPomodoroTime(time: String) {
        // Save time on milliseconds
        setPomodoroTime(time.toLongOrNull() ?: 0)
    }

    private fun setPomodoroTime(time: Long) {
        viewModelScope.launch(ioDispatcher) {
            _pomodoroTime.postValue(time)
            // Parse to milliseconds
            setWorkTimeUseCase.invoke(time.minutesToMilliseconds())
        }
    }

    /**
     * @param time: expressed in minutes
     */
    fun setRestTime(time: String) {
        // Save time on milliseconds
        setRestTime(time.toLongOrNull() ?: 0)
    }

    private fun setRestTime(time: Long) {
        viewModelScope.launch(ioDispatcher) {
            _restTime.postValue(time)
            // Parse to milliSeconds
            setRestTimeUseCase.invoke(time.minutesToMilliseconds())
        }
    }

    fun setAutoPlay(autoPlay: Boolean) {
        viewModelScope.launch {
            _autoPlay.value = autoPlay
            setAutoPlayUseCase.invoke(autoPlay)
        }
    }

    fun setKeepScreenOn(value: Boolean) {
        viewModelScope.launch {
            _keepScreenOn.value = value
            setKeepScreenOnUseCase.invoke(value)
        }
    }

    fun setVibration(value: Boolean) {
        viewModelScope.launch {
            _vibrationEnabled.value = value
            setVibrationUseCase.invoke(value)
        }
    }

    fun closeSettings() {
        _closeSettings.value = true
    }
}
