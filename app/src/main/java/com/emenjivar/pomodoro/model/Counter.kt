package com.emenjivar.pomodoro.model

open class Counter(
    val workTime: Long,
    val restTime: Long,
    var phase: Phase,
    var countDown: Long, // Start from workTime|restTime to 0
    private var progress: Float // Start from 100.0 to 0.0
) {

    fun getProgress() = when (phase) {
        Phase.WORK -> countDown * 100 / workTime.toFloat()
        Phase.REST -> countDown * 100 / restTime.toFloat()
    }

    fun setRest() {
        phase = Phase.REST
        countDown = restTime
        progress = 100f
    }
}
