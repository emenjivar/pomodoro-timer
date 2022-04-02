package com.emenjivar.pomodoro.screens.countdown

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.pomodoro.model.NormalPomodoro
import com.emenjivar.pomodoro.model.Pomodoro
import com.emenjivar.pomodoro.model.RestPomodoro
import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime
import java.util.LinkedList
import java.util.Queue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownViewModel(
    private val setNighModeUseCase: SetNighModeUseCase,
    private val isNightModeUseCase: IsNightModeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private val _pomodoro = MutableLiveData(NormalPomodoro() as Pomodoro)
    val pomodoro: LiveData<Pomodoro> = _pomodoro

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _isNightMode = MutableLiveData(true)
    val isNightMode: LiveData<Boolean> = _isNightMode

    private val _openSettings = MutableLiveData(false)
    val openSettings = _openSettings

    val listPomodoro: Queue<Pomodoro> = LinkedList()

    var startForBeginning: Boolean = true
    var testMode = false

    init {
        listPomodoro.add(NormalPomodoro())
        listPomodoro.add(RestPomodoro())

        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                _isNightMode.value = isNightModeUseCase.invoke()
            }
        }
    }

    fun startTimer(pomodoro: Pomodoro? = null) {
        if (pomodoro != null) {
            _isPlaying.value = true
            _pomodoro.value = pomodoro

            if (!testMode) {
                // Do not include this block on unitTesting
                countDownTimer = object : CountDownTimer(pomodoro.milliseconds, 500) {
                    override fun onTick(milliseconds: Long) {
                        setTime(
                            pomodoro = pomodoro,
                            milliseconds = milliseconds
                        )
                    }

                    override fun onFinish() {
                        nextPomodoro()
                    }
                }.start()
            }
        } else {
            val firstPomodoro = NormalPomodoro()
            setTime(
                pomodoro = firstPomodoro,
                milliseconds = firstPomodoro.totalMilliseconds
            )
        }
    }

    /**
     * Start pomodoro timer or resume a started one
     * when timer is resumed, the pomodoro is obtained from liveData.
     * this value has the updated time, progress and formatted time
     */
    fun playTimer() {
        if (startForBeginning) {
            startForBeginning = false
            startTimer(listPomodoro.poll())
        } else {
            startTimer(pomodoro.value)
        }
    }

    fun pauseTimer() {
        _isPlaying.value = false
        countDownTimer?.cancel()
    }

    fun nextPomodoro() {
        startTimer(listPomodoro.poll())
    }

    /**
     * Set current state of pomodoro on livedata value
     */
    fun setTime(pomodoro: Pomodoro, milliseconds: Long) {
        // Calculate progress on scale from 0.0 to 1.0
        val progress = TimerUtility.getProgress(
            currentTime = milliseconds,
            totalTime = pomodoro.totalMilliseconds
        ) / 100f

        _pomodoro.value = Pomodoro(
            milliseconds = milliseconds,
            totalMilliseconds = pomodoro.totalMilliseconds,
            time = milliseconds.formatTime(),
            progress = progress
        )
    }

    /**
     * This method stop the counter and load the value of current pomodoro
     * There are two times of pomodoro,
     *  the first is an 25min standard
     *  the seconds is a 5min rest pomodoro
     */
    fun stopCurrentPomodoro() {
        _isPlaying.value = false
        _pomodoro.value = when (_pomodoro.value) {
            is NormalPomodoro -> NormalPomodoro()
            is RestPomodoro -> RestPomodoro()
            else -> NormalPomodoro()
        }
        countDownTimer?.cancel()
    }

    fun toggleNightMode() {
        val nightMode = isNightMode.value?.not() ?: true
        _isNightMode.value = nightMode

        viewModelScope.launch(ioDispatcher) {
            setNighModeUseCase.invoke(nightMode)
        }
    }

    fun openSettings() {
        _openSettings.value = true
    }
}
