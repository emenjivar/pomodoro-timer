package com.emenjivar.pomodoro.countdown

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

class CountDownViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        countDownTimer = object : CountDownTimer(TimerUtility.POMODORO_TIME, 1000) {
            override fun onTick(milliseconds: Long) {
                Log.d("CountDownViewModel", "millis: $milliseconds, time: ${milliseconds.formatTime()}")
            }

            override fun onFinish() {
                Log.d("CountDownViewModel", "stopped")
            }
        }.start()
    }
}