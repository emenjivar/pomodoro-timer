package com.emenjivar.pomodoro.utils

import com.emenjivar.core.model.Pomodoro
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import java.util.concurrent.TimeUnit

fun Pomodoro.toCounter() = Counter(
    workTime = this.workTime,
    restTime = this.restTime,
    phase = Phase.WORK,
    countDown = this.workTime,
    progress = 100f
)

fun Float?.default(value: Float) = value

/**
 * Convert minutes input to millisecond time
 * useful to read values from UI
 */
fun Long.minutesToMilliseconds() = this * 100 * 60

/**
 * Convert milliseconds to time,
 * useful to show values of UI
 */
fun Long.millisecondsToMinutes() = this / 1000 / 60

/**
 * Convert milliseconds to readable time
 * 1500000ms -> 25:00
 */
fun Long?.formatTime() = String.format(
    "%02d:%02d",
    TimeUnit.MILLISECONDS.toMinutes(this ?: 0),
    TimeUnit.MILLISECONDS.toSeconds(this ?: 0) % 60
)
