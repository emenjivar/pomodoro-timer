package com.emenjivar.pomodoro.model

import com.emenjivar.pomodoro.utils.TimerUtility
import com.emenjivar.pomodoro.utils.TimerUtility.formatTime

open class Pomodoro(
    open var milliseconds: Long,
    open val totalMilliseconds: Long,
    open var time: String,
    open var progress: Float
)

data class NormalPomodoro(
    override var milliseconds: Long = TimerUtility.POMODORO_TIME,
    override val totalMilliseconds: Long = milliseconds,
    override var time: String = TimerUtility.POMODORO_TIME.formatTime(),
    override var progress: Float = 100f
) : Pomodoro(
    milliseconds = milliseconds,
    totalMilliseconds = totalMilliseconds,
    time = time,
    progress = progress
)

data class RestPomodoro(
    override var milliseconds: Long = TimerUtility.POMODORO_REST,
    override val totalMilliseconds: Long = milliseconds,
    override var time: String = TimerUtility.POMODORO_REST.formatTime(),
    override var progress: Float = 100f
) : Pomodoro(
    milliseconds = milliseconds,
    totalMilliseconds = totalMilliseconds,
    time = time,
    progress = progress
)
