package com.emenjivar.pomodoro.model

import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

data class Counter(
    val time: String,
    val progress: Float
) {
    companion object {
        fun default() = Counter(
            time = TimerUtility.POMODORO_TIME.formatTime(),
            progress = 100f
        )
    }
}