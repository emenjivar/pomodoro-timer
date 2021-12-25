package com.emenjivar.pomodoro.countdown

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

class CountDownViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null
    private val _counter = MutableLiveData(Counter())
    val counter: LiveData<Counter> = _counter

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun startTimer() {
        _isPlaying.value = true

        // Restore time before pause
        val pomodoroTime = _counter.value?.milliseconds ?: TimerUtility.POMODORO_TIME

        countDownTimer = object : CountDownTimer(pomodoroTime, 1000) {
            override fun onTick(milliseconds: Long) {
                _counter.value = Counter(
                    milliseconds = milliseconds,
                    time = milliseconds.formatTime(),
                    progress = TimerUtility.getProgress(milliseconds) / 100f
                )
                Log.d("CountDownViewModel", "millis: $milliseconds, data: ${_counter.value}")
            }

            override fun onFinish() {
                Log.d("CountDownViewModel", "stopped")
            }
        }.start()
    }

    fun pauseTimer() {
        _isPlaying.value = false
        countDownTimer?.cancel()
    }
}