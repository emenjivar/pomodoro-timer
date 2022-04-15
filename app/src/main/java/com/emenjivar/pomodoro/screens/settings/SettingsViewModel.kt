package com.emenjivar.pomodoro.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.SetPomodoroTimeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.pomodoro.utils.millisecondsToMinutes
import com.emenjivar.pomodoro.utils.minutesToMilliseconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPomodoroTimeUseCase: GetPomodoroTimeUseCase,
    private val setPomodoroTimeUseCase: SetPomodoroTimeUseCase,
    private val getRestTimeUseCase: GetRestTimeUseCase,
    private val setRestTimeUseCase: SetRestTimeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testMode: Boolean = false
) : ViewModel() {

    private val _pomodoroTime = MutableLiveData(0L)
    val pomodoroTime = _pomodoroTime

    private val _restTime = MutableLiveData(0L)
    val restTime = _restTime

    private val _closeSettings = MutableLiveData(false)
    val closeSettings = _closeSettings

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
        _pomodoroTime.postValue(getPomodoroTimeUseCase.invoke().millisecondsToMinutes())
        _restTime.postValue(getRestTimeUseCase.invoke().millisecondsToMinutes())
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
            setPomodoroTimeUseCase.invoke(time.minutesToMilliseconds())
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
            setRestTimeUseCase.invoke(time.millisecondsToMinutes())
        }
    }

    fun closeSettings() {
        _closeSettings.value = true
    }
}
