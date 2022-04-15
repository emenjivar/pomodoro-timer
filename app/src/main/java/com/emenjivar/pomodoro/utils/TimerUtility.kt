package com.emenjivar.pomodoro.utils

object TimerUtility {

    // Default value of pomodoro is 25 minutes
    const val WORK_TIME: Long = 1000 * 60 * 25

    /**
     * Calculate percent in pomodoro elapsed time
     */
    fun getProgress(currentTime: Long, totalTime: Long = WORK_TIME): Long =
        currentTime * 100 / totalTime
}
