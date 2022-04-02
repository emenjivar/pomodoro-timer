package com.emenjivar.pomodoro.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.SetPomodoroTimeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPomodoroTimeUseCase: GetPomodoroTimeUseCase,
    private val setPomodoroTimeUseCase: SetPomodoroTimeUseCase,
    private val getRestTimeUseCase: GetRestTimeUseCase,
    private val setRestTimeUseCase: SetRestTimeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _pomodoroTime = MutableLiveData(0L)
    val pomodoroTime = _pomodoroTime

    private val _restTime = MutableLiveData(0L)
    val restTime = _restTime

    private val _closeSettings = MutableLiveData(false)
    val closeSettings = _closeSettings

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch(ioDispatcher) {
            _pomodoroTime.postValue(getPomodoroTimeUseCase.invoke())
            _restTime.postValue(getRestTimeUseCase.invoke())
        }
    }

    fun setPomodoroTime(time: String) {
        setPomodoroTime(time.toLongOrNull() ?: 0)
    }

    private fun setPomodoroTime(time: Long) {
        viewModelScope.launch(ioDispatcher) {
            setPomodoroTimeUseCase.invoke(time)
            _pomodoroTime.postValue(time)
        }
    }

    fun setRestTime(time: String) {
        setRestTime(time.toLongOrNull() ?: 0)
    }

    private fun setRestTime(time: Long) {
        viewModelScope.launch(ioDispatcher) {
            setRestTimeUseCase.invoke(time)
            _restTime.postValue(time)
        }
    }

    fun closeSettings() {
        _closeSettings.value = true
    }
}
