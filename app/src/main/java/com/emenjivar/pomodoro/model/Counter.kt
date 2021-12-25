package com.emenjivar.pomodoro.model

import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

data class Counter(
    val milliseconds: Long = TimerUtility.POMODORO_TIME,
    val time: String = TimerUtility.POMODORO_TIME.formatTime(),
    val progress: Float = 100f
)