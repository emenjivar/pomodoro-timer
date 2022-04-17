package com.emenjivar.pomodoro.model

open class Counter(
    val workTime: Long,
    val restTime: Long,
    var phase: Phase,
    var countDown: Long, // Start from workTime|restTime to 0
) {

    fun getProgress(): Float {
        val progress = when (phase) {
            Phase.WORK -> countDown * 100 / workTime.toFloat()
            Phase.REST -> countDown * 100 / restTime.toFloat()
        }
        return progress
    }

    /**
     * @return percent using 1.0 scale [0 to 1.0]
     */
    fun getScaleProgress(): Float = getProgress() / 100f

    fun setRest() {
        phase = Phase.REST
        countDown = restTime
    }
}
