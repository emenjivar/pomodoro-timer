package com.emenjivar.pomodoro.utils

fun countDownInterval(ms: Long) = when {
    // ms <= 5:00 min
    ms <= 5L.minutesToMilliseconds() -> 500L
    // ms > 5:00 min
    else -> 1000L
}
