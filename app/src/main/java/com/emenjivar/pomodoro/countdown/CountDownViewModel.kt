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
    private val _counter = MutableLiveData(Counter.default())
    val counter: LiveData<Counter> = _counter

    fun startTimer() {
        countDownTimer = object : CountDownTimer(TimerUtility.POMODORO_TIME, 1000) {
            override fun onTick(milliseconds: Long) {
                _counter.value = Counter(
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
}