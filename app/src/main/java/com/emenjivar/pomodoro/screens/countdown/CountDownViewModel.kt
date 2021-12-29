package com.emenjivar.pomodoro.screens.countdown

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emenjivar.pomodoro.model.Pomodoro
import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

class CountDownViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null
    private val _counter = MutableLiveData(Pomodoro())
    val counter: LiveData<Pomodoro> = _counter

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _isFullScreen = MutableLiveData(false)
    val isFullScreen = _isFullScreen

    var testMode = false

    fun startTimer() {
        _isPlaying.value = true

        // Restore time before pause
        val pomodoroTime = _counter.value?.milliseconds ?: TimerUtility.POMODORO_TIME

        if(!testMode) {
            // Do not include this block on unitTesting
            countDownTimer = object : CountDownTimer(pomodoroTime, 1000) {
                override fun onTick(milliseconds: Long) {
                    setTime(milliseconds)
                    Log.d("CountDownViewModel", "millis: $milliseconds, data: ${_counter.value}")
                }

                override fun onFinish() {
                    stopTimer()
                    Log.d("CountDownViewModel", "stopped")
                }
            }.start()
        }
    }

    fun setTime(milliseconds: Long) {
        _counter.value = Pomodoro(
            milliseconds = milliseconds,
            time = milliseconds.formatTime(),
            // Value between 0 and 1
            progress = TimerUtility.getProgress(milliseconds) / 100f
        )
    }

    fun pauseTimer() {
        _isPlaying.value = false
        countDownTimer?.cancel()
    }

    // Reset to default values and pause
    fun stopTimer() {
        _isPlaying.value = false
        _counter.value = Pomodoro()
        countDownTimer?.cancel()
    }

    fun toggleNightMode() {
        _isFullScreen.value = _isFullScreen.value?.not()
    }
}