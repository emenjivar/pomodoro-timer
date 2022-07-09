package com.emenjivar.pomodoro.screens.settings.time

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.core.usecase.SetWorkTimeUseCase
import com.emenjivar.pomodoro.utils.formatTime
import com.emenjivar.pomodoro.utils.toSafeInt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsTimeViewModel(
    private val getPomodoroUseCase: GetPomodoroUseCase,
    private val setWorkTimeUseCase: SetWorkTimeUseCase,
    private val setRestTimeUseCase: SetRestTimeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _workTime = mutableStateOf(0L)
    val workTime: State<Long> = _workTime

    private val _restTime = mutableStateOf(0L)
    val restTime: State<Long> = _restTime

    // time string on format hhssmm
    private var timeString = ""

    private val _hours = mutableStateOf("00")
    val hours: State<String> = _hours

    private val _minutes = mutableStateOf("00")
    val minutes: State<String> = _minutes

    private val _seconds = mutableStateOf("00")
    val seconds: State<String> = _seconds

    fun loadTimeValuesOnSettings() = viewModelScope.launch(ioDispatcher) {
        val pomodoro = getPomodoroUseCase.invoke()
        _workTime.value = pomodoro.workTime
        _restTime.value = pomodoro.restTime
    }

    fun loadTimeOnModal(isPomodoro: Boolean) = viewModelScope.launch(ioDispatcher) {
        // Load pomodoro or rest time on keyboard
        val time =
            if (isPomodoro) _workTime.value
            else _restTime.value

        loadMillisecondsTime(time)
    }

    fun saveTime(isPomodoro: Boolean) = viewModelScope.launch(ioDispatcher) {
        val milliseconds = getMilliseconds()
        if (isPomodoro)
            setWorkTimeUseCase.invoke(milliseconds)
        else
            setRestTimeUseCase.invoke(milliseconds)

        loadTimeValuesOnSettings()
    }

    /**
     * 2 -> 00h 00m 02s
     * 25 -> 00h 00m 25s
     * 250 -> 00h 02m 50s
     * 2507 -> 00h 25m 07s
     */
    fun onInputChange(digit: Int) {
        if (timeString.length < 6) {
            timeString += digit.toString()
            setTimeValues(timeString)
        }
    }

    fun onBackSpace() {
        if (timeString.isNotEmpty()) {
            timeString = timeString.substring(0, timeString.length - 1)
            setTimeValues(timeString)
        }
    }

    /**
     * @param time: time string in format hhmmss
     * Split and load hours, minutes and seconds livedata values
     */
    private fun setTimeValues(time: String) {
        val pad = time.padStart(6, '0')
        val hours = pad.substring(0, 2)
        val minutes = pad.substring(2, 4)
        val seconds = pad.substring(4, 6)

        _hours.value = hours
        _minutes.value = minutes
        _seconds.value = seconds
    }

    fun getMilliseconds(): Long =
        1000 * (3600 * hours.value.toSafeInt() + 60 * minutes.value.toSafeInt() + seconds.value.toSafeInt()).toLong()

    /**
     * Convert milliseconds to hhmmss format
     * and load values on states
     */
    fun loadMillisecondsTime(time: Long) {
        var convert = time.formatTime()
        convert = convert.replace(":", "")

        // This value store input time from keyboard
        this.timeString = convert
        setTimeValues(convert)
    }
}
