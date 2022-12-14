package com.emenjivar.pomodoro.ui.screens.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.pomodoro.data.SettingsRepository
import com.emenjivar.pomodoro.usecases.GetColorUseCase
import com.emenjivar.pomodoro.usecases.SetColorUseCase
import com.emenjivar.pomodoro.usecases.GetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.SetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.IsKeepScreenOnUseCase
import com.emenjivar.pomodoro.usecases.SetKeepScreenOnUseCase
import com.emenjivar.pomodoro.usecases.IsVibrationEnabledUseCase
import com.emenjivar.pomodoro.usecases.SetVibrationUseCase
import com.emenjivar.pomodoro.usecases.AreSoundsEnableUseCase
import com.emenjivar.pomodoro.usecases.SetSoundsEnableUseCase
import com.emenjivar.pomodoro.data.SharedSettingsRepository
import com.emenjivar.pomodoro.data.model.StructTime
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.utils.ThemeColor
import com.emenjivar.pomodoro.utils.formatTime
import com.emenjivar.pomodoro.utils.model.Counter
import com.emenjivar.pomodoro.utils.toCounter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sharedSettingsRepository: SharedSettingsRepository,
    private val settingsRepository: SettingsRepository,
    private val getColorUseCase: GetColorUseCase,
    private val setColorUseCase: SetColorUseCase,
    private val getAutoPlayUseCase: GetAutoPlayUseCase,
    private val setAutoPlayUseCase: SetAutoPlayUseCase,
    private val isKeepScreenOnUseCase: IsKeepScreenOnUseCase,
    private val setKeepScreenOnUseCase: SetKeepScreenOnUseCase,
    private val isVibrationEnabledUseCase: IsVibrationEnabledUseCase,
    private val setVibrationUseCase: SetVibrationUseCase,
    private val areSoundsEnableUseCase: AreSoundsEnableUseCase,
    private val setSoundsEnableUseCase: SetSoundsEnableUseCase,
    private val customVibrator: CustomVibrator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    testMode: Boolean = false
) : ViewModel() {

    private val colorTheme: MutableStateFlow<Color> = MutableStateFlow(ThemeColor.Tomato.color)

    private val workTime = MutableStateFlow(0L)
    private val restTime = MutableStateFlow(0L)
    private val structTime = MutableStateFlow(StructTime.empty())

    private val counter = MutableStateFlow<Counter?>(null)

    val uiState = SettingsUIState(
        colorTheme = colorTheme,
        workTime = workTime,
        restTime = restTime,
        structTime = structTime,
        loadModalTime = ::loadModalTime,
        onInputChange = ::onInputChange,
        onBackSpace = ::onBackSpace,
        onSaveTime = ::onSaveTime
    )

    private fun onBackSpace() {
        if (structTime.value.timeString.isNotEmpty()) {
            val updatedTimeString = structTime.value.timeString.substring(0, structTime.value.timeString.length - 1)
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

    private val _vibrationEnabled = mutableStateOf(false)
    val vibrationEnabled: State<Boolean> = _vibrationEnabled

    private val _soundsEnable = mutableStateOf(true)
    val soundsEnable: State<Boolean> = _soundsEnable

    private val _selectedColor = MutableLiveData<Int?>(null)
    val selectedColor: LiveData<Int?> = _selectedColor

    init {
        settingsRepository.getPomodoro().onEach { pomodoro ->
            workTime.update { pomodoro.workTime }
            restTime.update { pomodoro.restTime }
        }.launchIn(viewModelScope)

        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadSettings()
            }
        }
    }

    suspend fun loadSettings() {
        _selectedColor.postValue(getColorUseCase.invoke())
        /**
         * Values are expressed in milliseconds
         * transform to minutes to show a readable value on UI
         */
//        _autoPlay.value = getAutoPlayUseCase.invoke()
//        _keepScreenOn.value = isKeepScreenOnUseCase.invoke()
//        _vibrationEnabled.value = isVibrationEnabledUseCase.invoke()
//        _soundsEnable.value = areSoundsEnableUseCase.invoke()
    }

    fun setColor(value: Color) {
//        _selectedColor.value = value
        colorTheme.update { value }
        sharedSettingsRepository.setColorTheme(value)

        viewModelScope.launch(ioDispatcher) {
            customVibrator.click()
//            setColorUseCase.invoke(value)
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

    fun setSoundsEnable(value: Boolean) {
        _soundsEnable.value = value

        viewModelScope.launch(ioDispatcher) {
            setSoundsEnableUseCase.invoke(value)
        }
    }

    fun closeSettings() {
        _closeSettings.value = true
    }
}
