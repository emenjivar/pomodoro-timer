package com.emenjivar.pomodoro.utils

import java.util.concurrent.TimeUnit

object TimerUtility {

    // Value on milliseconds
    const val POMODORO_TIME: Long = 1000 * 60 * 25
    private const val TIME_FORMAT = "%02d:%02d"

    // Convert milliseconds to readable time
    fun Long.formatTime() = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )
}