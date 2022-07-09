package com.emenjivar.pomodoro.utils

import com.emenjivar.core.model.Pomodoro
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import java.util.Locale.US
import java.util.concurrent.TimeUnit

fun Pomodoro.toCounter() = Counter(
    workTime = this.workTime,
    restTime = this.restTime,
    phase = Phase.WORK,
    countDown = this.workTime
)

/**
 * Convert minutes input to millisecond time
 * useful to read values from UI
 */
fun Long.minutesToMilliseconds() = this * 1000 * 60

/**
 * Convert milliseconds to time,
 * useful to show values of UI
 */
fun Long.millisecondsToMinutes() = this / 1000 / 60

/**
 * Convert long milliseconds to formatted time
 * < 1 min = ss
 * < 60 min = mm:ss
 * >= 60 min = hh:mm:ss
 */
fun Long?.formatTime() = when {
    this == null -> "00:00:00"
    // formatting seconds
    this < 60000 -> String.format(
        locale = US,
        format = "%02d",
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )
    // formatting minutes:seconds
    this < 3600000 -> String.format(
        locale = US,
        format = "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )
    // formatting hours:minutes:seconds
    else -> String.format(
        locale = US,
        format = "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(this),
        TimeUnit.MILLISECONDS.toMinutes(this) % 60,
        TimeUnit.MILLISECONDS.toSeconds(this) % 60 % 60
    )
}

fun String.toSafeInt() = this.toIntOrNull() ?: 0

/**
 * Convert milliseconds to splitTime object
 * ej. 90649000ms -> SplitTime(hours = "25", minutes = "10", seconds = "49)
 */
fun Long.toSplitTime(): SplitTime {
    val hours = this / 3600000

    // milliseconds without hours
    val reminder = this % 3600000
    val minutes = reminder / 60000

    // milliseconds without hours and minutes
    val seconds = reminder % 60000 / 1000

    return SplitTime(
        hours = hours.toString().padStart(2, '0'),
        minutes = minutes.toString().padStart(2, '0'),
        seconds = seconds.toString().padStart(2, '0')
    )
}
