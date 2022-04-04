package com.emenjivar.pomodoro.screens.countdown

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
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
    private val getPomodoroTimeUseCase: GetPomodoroTimeUseCase,
    private val getRestTimeUseCase: GetRestTimeUseCase,
    private val setNighModeUseCase: SetNighModeUseCase,
    private val isNightModeUseCase: IsNightModeUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val testMode: Boolean = false
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

    init {
        if (!testMode) {
            viewModelScope.launch(ioDispatcher) {
                loadDefaultValues()
            }
        }
    }

    suspend fun loadDefaultValues() {
        _isNightMode.postValue(isNightModeUseCase.invoke())

        // Set default pomodoro and load on livedata
        val defaultPomodoro = getDefaultPomodoro()
        _pomodoro.postValue(defaultPomodoro)

        // Load time from useCases
        listPomodoro.add(defaultPomodoro)
        listPomodoro.add(getDefaultRestPomodoro())
    }

    /**
     * Get time values from dataStorage
     */
    private suspend fun getDefaultPomodoro(): Pomodoro {
        val time = getPomodoroTimeUseCase.invoke()
        return NormalPomodoro(
            milliseconds = time,
            time = time.formatTime(),
            totalMilliseconds = time,
            progress = 100f
        )
    }

    /**
     * Get time values from dataStorage
     */
    private suspend fun getDefaultRestPomodoro(): Pomodoro {
        val time = getRestTimeUseCase.invoke()
        return RestPomodoro(
            milliseconds = time,
            time = time.formatTime(),
            totalMilliseconds = time,
            progress = 100f
        )
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
            stopCurrentPomodoro()
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
     * Stop the counter and load default pomodoro
     * from dataStorage time values.
     */
    fun stopCurrentPomodoro() {
        viewModelScope.launch(ioDispatcher) {
            _isPlaying.postValue(false)
            /**
             * Always load normal pomodoro,
             * even if rest pomodoro is playing
             */
            _pomodoro.postValue(getDefaultPomodoro())
            countDownTimer?.cancel()
        }
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
