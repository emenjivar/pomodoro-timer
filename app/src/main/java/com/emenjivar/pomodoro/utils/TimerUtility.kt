package com.emenjivar.pomodoro.utils

import java.util.concurrent.TimeUnit

object TimerUtility {

    // Default value of pomodoro is 25 minutes
    const val POMODORO_TIME: Long = 1000 * 60 * 25
    private const val TIME_FORMAT = "%02d:%02d"

    // Default value of pomodoro rest is 5 minutes
    const val POMODORO_REST: Long = 1000 * 60 * 5

    /**
     * Convert milliseconds to readable time
     * 1500000ms -> 25:00
     */
    fun Long.formatTime() = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

    /**
     * Calculate percent in pomodoro elapsed time
     */
    fun getProgress(currentTime: Long, totalTime: Long = POMODORO_TIME): Long = currentTime * 100 / totalTime
}