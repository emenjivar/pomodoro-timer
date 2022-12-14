package com.emenjivar.pomodoro.ui.screens.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.pomodoro.data.SettingsRepository
import com.emenjivar.pomodoro.usecases.SetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.SetKeepScreenOnUseCase
import com.emenjivar.pomodoro.data.SharedSettingsRepository
import com.emenjivar.pomodoro.data.model.StructTime
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.formatTime
import com.emenjivar.pomodoro.utils.model.Counter
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sharedSettingsRepository: SharedSettingsRepository,
    private val settingsRepository: SettingsRepository,
    private val setAutoPlayUseCase: SetAutoPlayUseCase,
    private val setKeepScreenOnUseCase: SetKeepScreenOnUseCase,
    private val customVibrator: CustomVibrator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testMode: Boolean = false
) : ViewModel() {

    private val colorTheme: MutableStateFlow<Color> = MutableStateFlow(ThemeColor.Tomato.color)
    private val workTime = MutableStateFlow(0L)
    private val restTime = MutableStateFlow(0L)
    private val structTime = MutableStateFlow(StructTime.empty())
    private val counter = MutableStateFlow<Counter?>(null)
    private val enableSound = settingsRepository.areSoundsEnabled()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )
    private val enableVibration = settingsRepository.isVibrationEnabled()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    val uiState = SettingsUIState(
        colorTheme = colorTheme,
        workTime = workTime,
        restTime = restTime,
        structTime = structTime,
        enableSound = enableSound,
        enableVibration = enableVibration,
        loadModalTime = ::loadModalTime,
        onInputChange = ::onInputChange,
        onBackSpace = ::onBackSpace,
        onSaveTime = ::onSaveTime,
        onEnableSound = ::onEnableSound,
        onEnableVibration = ::onEnableVibration
    )

    private fun onBackSpace() {
        if (structTime.value.timeString.isNotEmpty()) {
            val updatedTimeString =
                structTime.value.timeString.substring(0, structTime.value.timeString.length - 1)
            setTimeValues(updatedTimeString)
        }
    }

    private fun loadModalTime(isPomodoro: Boolean) {
        val time = if (isPomodoro) counter.value?.workTime else counter.value?.restTime
        time?.let { safeTime ->
            val convert = safeTime.formatTime().run { replace(":", "") }
            setTimeValues(convert)
        }
    }

    private fun setTimeValues(time: String) {
        val pad = time.padStart(6, '0')
        val hours = pad.substring(0, 2)
        val minutes = pad.substring(2, 4)
        val seconds = pad.substring(4, 6)

        structTime.update {
            StructTime(
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                timeString = time
            )
        }
    }

    private fun onInputChange(digit: Int) {
        if (structTime.value.timeString.length < 6) {
            val updatedTimeString = structTime.value.timeString + digit.toString()
            setTimeValues(updatedTimeString)
        }
    }

    private fun onSaveTime(isPomodoro: Boolean) = viewModelScope.launch {
        val milliseconds = structTime.value.getMilliseconds()
        if (isPomodoro) {
            settingsRepository.setWorkTime(milliseconds)
        } else {
            settingsRepository.setRestTime(milliseconds)
        }
    }

    private fun onEnableSound(enableSound: Boolean) = viewModelScope.launch {
        settingsRepository.setSounds(enableSound)
    }

    private fun onEnableVibration(enableVibration: Boolean) = viewModelScope.launch {
        settingsRepository.setVibration(enableVibration)
    }

    init {
        settingsRepository.getPomodoro().onEach { pomodoro ->
            counter.update { pomodoro.toCounter() }
        }.launchIn(viewModelScope)
    }

    private val _closeSettings = MutableLiveData(false)
    val closeSettings = _closeSettings

    private val _autoPlay = mutableStateOf(false)
    val autoPlay: State<Boolean> = _autoPlay

    private val _keepScreenOn = mutableStateOf(false)
    val keepScreenOn: State<Boolean> = _keepScreenOn

    init {
        settingsRepository.getPomodoro().onEach { pomodoro ->
            workTime.update { pomodoro.workTime }
            restTime.update { pomodoro.restTime }
        }.launchIn(viewModelScope)
    }

    fun setColor(value: Color) {
        colorTheme.update { value }
        sharedSettingsRepository.setColorTheme(value)

        viewModelScope.launch(ioDispatcher) {
            customVibrator.click()
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

    fun closeSettings() {
        _closeSettings.value = true
    }
}
